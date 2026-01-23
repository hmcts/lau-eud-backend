package uk.gov.hmcts.reform.laubackend.eud.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.laubackend.eud.domain.IdamUserChangeAudit;
import uk.gov.hmcts.reform.laubackend.eud.dto.UserUpdate;
import uk.gov.hmcts.reform.laubackend.eud.repository.IdamUserChangeAuditCustomRepository;
import uk.gov.hmcts.reform.laubackend.eud.repository.IdamUserChangeAuditRepository;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUpdatesServiceTest {

    @Mock
    private IdamUserChangeAuditCustomRepository customRepository;

    @Mock
    private IdamUserChangeAuditRepository repository;

    @InjectMocks
    private UserUpdatesService service;

    private static final String USER_ID = "user-123";
    private static final String ENCRYPTION_KEY = "test-key";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "encryptionKey", ENCRYPTION_KEY);
    }

    @Test
    void shouldUseCustomRepositoryWhenEncryptionEnabled() {
        ReflectionTestUtils.setField(service, "encryptionEnabled", true);

        Pageable pageable = PageRequest.of(1, 20);
        UserUpdate u1 = mock(UserUpdate.class);
        Page<UserUpdate> expected = new PageImpl<>(List.of(u1), pageable, 1);

        when(customRepository.findIdamUserChangeAuditsByUserId(USER_ID, pageable, ENCRYPTION_KEY))
            .thenReturn(expected);

        Page<UserUpdate> result = service.getUserUpdates(USER_ID, pageable);

        assertThat(result).isSameAs(expected);
        verify(customRepository).findIdamUserChangeAuditsByUserId(USER_ID, pageable, ENCRYPTION_KEY);
        verifyNoInteractions(repository);
    }

    @Test
    void shouldUseJpaRepositoryAndMapToDtoWhenEncryptionDisabled() {
        ReflectionTestUtils.setField(service, "encryptionEnabled", false);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("event_timestamp"));
        // Use mocks for entities; mapping is performed by UserUpdate::from
        IdamUserChangeAudit a1 = mock(IdamUserChangeAudit.class);
        IdamUserChangeAudit a2 = mock(IdamUserChangeAudit.class);
        Page<IdamUserChangeAudit> page = new PageImpl<>(List.of(a1, a2), pageable, 2);

        when(repository.findIdamUserChangeAuditsByUserId(USER_ID, pageable)).thenReturn(page);

        Page<UserUpdate> result = service.getUserUpdates(USER_ID, pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).allMatch(Objects::nonNull);

        verify(repository).findIdamUserChangeAuditsByUserId(USER_ID, pageable);
        verifyNoInteractions(customRepository);
    }

    @Test
    void shouldTreatNullEncryptionEnabledAsDisabled() {
        ReflectionTestUtils.setField(service, "encryptionEnabled", null);

        Pageable pageable = PageRequest.of(0, 5, Sort.by("event_timestamp"));
        Page<IdamUserChangeAudit> page = new PageImpl<>(List.of(mock(IdamUserChangeAudit.class)), pageable, 1);

        when(repository.findIdamUserChangeAuditsByUserId(USER_ID, pageable)).thenReturn(page);

        Page<UserUpdate> result = service.getUserUpdates(USER_ID, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(repository).findIdamUserChangeAuditsByUserId(USER_ID, pageable);
        verifyNoInteractions(customRepository);
    }
}
