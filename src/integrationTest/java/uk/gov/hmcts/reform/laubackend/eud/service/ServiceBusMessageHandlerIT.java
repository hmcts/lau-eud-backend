package uk.gov.hmcts.reform.laubackend.eud.service;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.laubackend.eud.domain.AccountStatus;
import uk.gov.hmcts.reform.laubackend.eud.domain.EventType;
import uk.gov.hmcts.reform.laubackend.eud.domain.IdamUserChangeAudit;
import uk.gov.hmcts.reform.laubackend.eud.domain.RecordType;
import uk.gov.hmcts.reform.laubackend.eud.dto.IdamEvent;
import uk.gov.hmcts.reform.laubackend.eud.dto.User;
import uk.gov.hmcts.reform.laubackend.eud.repository.IdamUserChangeAuditRepository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServiceBusMessageHandlerIT {

    private static final String USER_ID = "13e31622-edea-493c-8240-9b780c9d6111";
    private static final String PRINCIPAL_ID = "principal-123";

    @Autowired
    private Flyway flyway;

    @Autowired
    private ServiceBusMessageHandler handler;

    @Autowired
    private IdamUserChangeAuditRepository repository;

    @BeforeAll
    void migrateSchema() {
        flyway.migrate();
    }

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void handleMessage_persistsUserChanges() {
        User previousUser = baseUser("old.email@example.org");
        User currentUser = baseUser("new.email@example.org");

        IdamEvent event = new IdamEvent(
            EventType.MODIFY,
            "lau-client",
            PRINCIPAL_ID,
            currentUser,
            previousUser,
            LocalDateTime.of(2024, 1, 1, 10, 15, 30)
        );

        handler.handleMessage(event);

        List<IdamUserChangeAudit> changes = repository.findAll();
        assertThat(changes).isNotEmpty();
        assertThat(changes)
            .anySatisfy(change -> {
                assertThat(change.getUserId()).isEqualTo(USER_ID);
                assertThat(change.getPrincipalUserId()).isEqualTo(PRINCIPAL_ID);
                assertThat(change.getEventType()).isEqualTo(EventType.MODIFY);
                assertThat(change.getEventName()).isEqualTo("email");
                assertThat(change.getEventValue()).isEqualTo("new.email@example.org");
                assertThat(change.getPreviousEventValue()).isEqualTo("old.email@example.org");
                assertThat(change.getEventTimestamp()).isEqualTo(
                    OffsetDateTime.of(2024, 1, 1, 10, 15, 30, 0, ZoneOffset.UTC)
                );
            });
    }

    private User baseUser(String email) {
        return new User(
            email,
            USER_ID,
            "John",
            "Smith",
            "John Smith",
            List.of("citizen"),
            "sso-1",
            "provider",
            AccountStatus.ACTIVE,
            RecordType.LIVE,
            OffsetDateTime.parse("2023-06-21T13:28:40.966619Z"),
            OffsetDateTime.parse("2023-06-22T13:28:40.966619Z"),
            null,
            OffsetDateTime.parse("2023-06-23T13:28:40.966619Z")
        );
    }
}
