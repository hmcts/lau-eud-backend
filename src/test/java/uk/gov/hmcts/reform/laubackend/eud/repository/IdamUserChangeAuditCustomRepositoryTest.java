package uk.gov.hmcts.reform.laubackend.eud.repository;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import uk.gov.hmcts.reform.laubackend.eud.domain.EventType;
import uk.gov.hmcts.reform.laubackend.eud.domain.IdamUserChangeAudit;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class IdamUserChangeAuditCustomRepositoryTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:16-alpine");

    private static final String KEY = "test-encryption-key";

    @Autowired
    private IdamUserChangeAuditCustomRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    void saveAllEncrypted_shouldInsertRowsAndEncryptColumns() {
        // given
        IdamUserChangeAudit a1 = Mockito.mock(IdamUserChangeAudit.class);
        when(a1.getUserId()).thenReturn("user-1");
        when(a1.getPrincipalUserId()).thenReturn("principal-1");
        when(a1.getEventType()).thenReturn(EventType.MODIFY);
        when(a1.getEventName()).thenReturn("email");
        when(a1.getEventValue()).thenReturn("new@example.com");
        when(a1.getPreviousEventValue()).thenReturn("old@example.com");
        when(a1.getEventTimestamp()).thenReturn(Instant.parse("2026-01-19T10:00:00Z").atOffset(ZoneOffset.UTC));

        IdamUserChangeAudit a2 = Mockito.mock(IdamUserChangeAudit.class);
        when(a2.getUserId()).thenReturn("user-2");
        when(a2.getPrincipalUserId()).thenReturn("principal-2");
        when(a2.getEventType()).thenReturn(EventType.ADD);
        when(a2.getEventName()).thenReturn("ip");
        when(a2.getEventValue()).thenReturn("1.2.3.4");
        when(a2.getPreviousEventValue()).thenReturn(null);
        when(a2.getEventTimestamp()).thenReturn(Instant.parse("2026-01-19T11:00:00Z").atOffset(ZoneOffset.UTC));

        // when
        repository.saveAllEncrypted(List.of(a1, a2), KEY);

        // then
        Integer count = jdbcTemplate.queryForObject(
            "select count(*) from public.idam_user_change_audit",
            Integer.class
        );
        assertThat(count).isEqualTo(2);

        String encryptedValue = namedParameterJdbcTemplate.queryForObject(
            "select event_value from public.idam_user_change_audit where user_id = :userId",
            new MapSqlParameterSource("userId", "user-1"),
            String.class
        );
        assertThat(encryptedValue)
            .isNotNull()
            .doesNotContain("new@example.com");

        // add repository call to fetch decrypted values once that is implemented
    }

    @Test
    void saveAllEncrypted_shouldDoNothingForEmptyList() {
        repository.saveAllEncrypted(List.of(), KEY);

        Integer count = jdbcTemplate.queryForObject(
            "select count(*) from public.idam_user_change_audit",
            Integer.class
        );
        assertThat(count).isZero();
    }
}

