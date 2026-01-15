package uk.gov.hmcts.reform.laubackend.eud.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.laubackend.eud.domain.EventType;
import uk.gov.hmcts.reform.laubackend.eud.domain.IdamUserChangeAudit;
import uk.gov.hmcts.reform.laubackend.eud.dto.IdamEvent;
import uk.gov.hmcts.reform.laubackend.eud.dto.User;
import uk.gov.hmcts.reform.laubackend.eud.repository.IdamUserChangeAuditCustomRepository;
import uk.gov.hmcts.reform.laubackend.eud.repository.IdamUserChangeAuditRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class ServiceBusMessageHandlerTest {

    @Mock
    private UserDiffService userDiffService;

    @Mock
    private IdamUserChangeAuditRepository repository;

    @Mock
    private IdamUserChangeAuditCustomRepository customRepository;

    @InjectMocks
    private ServiceBusMessageHandler handler;

    @Captor
    private ArgumentCaptor<List<IdamUserChangeAudit>> auditsCaptor;

    @Test
    void shouldSkipModifyWhenPreviousUserIsNull() {
        User currentUser = new UserTestData().build();

        IdamEvent event = newIdamEvent(
            EventType.MODIFY,
            null,
            currentUser,
            "principal-999",
            LocalDateTime.now(ZoneOffset.UTC)
        );

        handler.handleMessage(event);

        verify(userDiffService, never()).diffUsers(any(), any());
        verify(repository, never()).saveAll(any());
    }

    @Test
    void shouldSaveAuditRowsForEachFieldChange() {
        ReflectionTestUtils.setField(handler, "encryptionEnabled", false);
        User previousUser = new UserTestData().build();
        User currentUser = new UserTestData().build();

        LocalDateTime eventTime = LocalDateTime.now(ZoneOffset.UTC);

        IdamEvent event = newIdamEvent(
            EventType.MODIFY,
            previousUser,
            currentUser,
            "principal-123",
            eventTime
        );

        when(userDiffService.diffUsers(previousUser, currentUser)).thenReturn(List.of(
            new UserDiffService.FieldChange("accountStatus", "ACTIVE", "SUSPENDED"),
            new UserDiffService.FieldChange("roleNames", "a-role", "a-role,b-role")
        ));

        handler.handleMessage(event);

        verify(repository).saveAll(auditsCaptor.capture());
        List<IdamUserChangeAudit> saved = auditsCaptor.getValue();

        assertThat(saved).hasSize(2);

        IdamUserChangeAudit first = saved.getFirst();
        assertThat(first.getEventName()).isEqualTo("accountStatus");
        assertThat(first.getEventValue()).isEqualTo("SUSPENDED");
        assertThat(first.getPreviousEventValue()).isEqualTo("ACTIVE");

        IdamUserChangeAudit second = saved.get(1);
        assertThat(second.getEventName()).isEqualTo("roleNames");
        assertThat(second.getEventValue()).isEqualTo("a-role,b-role");
        assertThat(second.getPreviousEventValue()).isEqualTo("a-role");

        assertThat(first.getPrincipalUserId()).isEqualTo(second.getPrincipalUserId()).isEqualTo("principal-123");
        assertThat(first.getUserId()).isEqualTo(second.getUserId()).isEqualTo(currentUser.id());
    }

    @Test
    void shouldSaveAuditRowsWithEncryptionForEachFieldChange() {
        ReflectionTestUtils.setField(handler, "encryptionEnabled", true);
        ReflectionTestUtils.setField(handler, "encryptionKey", "dummy");
        User previousUser = new UserTestData().build();
        User currentUser = new UserTestData().build();

        LocalDateTime eventTime = LocalDateTime.now(ZoneOffset.UTC);

        IdamEvent event = newIdamEvent(
            EventType.MODIFY,
            previousUser,
            currentUser,
            "principal-999",
            eventTime
        );

        when(userDiffService.diffUsers(previousUser, currentUser)).thenReturn(List.of(
            new UserDiffService.FieldChange("accountStatus", "ACTIVE", "SUSPENDED")
        ));

        handler.handleMessage(event);

        verify(customRepository).saveAllEncrypted(auditsCaptor.capture(), ArgumentMatchers.eq("dummy"));
        List<IdamUserChangeAudit> saved = auditsCaptor.getValue();

        assertThat(saved).hasSize(1);

        IdamUserChangeAudit first = saved.getFirst();
        assertThat(first.getEventName()).isEqualTo("accountStatus");
        assertThat(first.getEventValue()).isEqualTo("SUSPENDED");
        assertThat(first.getPreviousEventValue()).isEqualTo("ACTIVE");
    }

    private static IdamEvent newIdamEvent(
        EventType eventType,
        User previousUser,
        User currentUser,
        String principalId,
        LocalDateTime eventDateTime
    ) {
        return new IdamEvent(eventType, "", principalId, currentUser, previousUser, eventDateTime);
    }
}
