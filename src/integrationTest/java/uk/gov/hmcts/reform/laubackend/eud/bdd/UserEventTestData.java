package uk.gov.hmcts.reform.laubackend.eud.bdd;

import uk.gov.hmcts.reform.laubackend.eud.domain.AccountStatus;
import uk.gov.hmcts.reform.laubackend.eud.domain.RecordType;
import uk.gov.hmcts.reform.laubackend.eud.dto.IdamEvent;
import uk.gov.hmcts.reform.laubackend.eud.dto.User;
import uk.gov.hmcts.reform.laubackend.eud.service.ServiceBusMessageHandler;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.List;

final class UserEventTestData {
    public static String CITIZEN_ROLE = "citizen";
    public static String CHANGED_EMAIL = "new.email@example.org";
    public static String CHANGED_FIRST_NAME = "Jane";
    public static String CHANGED_LAST_NAME = "Doe";
    public static String CHANGED_ROLE = "caseworker";


    private UserEventTestData() {
    }

    static User user(
        String email,
        String id,
        String forename,
        String surname,
        List<String> roleNames,
        AccountStatus accountStatus,
        RecordType recordType
    ) {
        return new User(
            email,
            id,
            forename,
            surname,
            forename + " " + surname,
            roleNames,
            "sso-1",
            "provider",
            accountStatus,
            recordType,
            OffsetDateTime.parse("2023-06-21T13:28:40.966619Z"),
            OffsetDateTime.parse("2023-06-22T13:28:40.966619Z"),
            null,
            OffsetDateTime.parse("2023-06-23T13:28:40.966619Z")
        );
    }

    static IdamEvent modifyEvent(
        String clientId,
        String principalId,
        User currentUser,
        User previousUser,
        LocalDateTime eventTime
    ) {
        return new IdamEvent(
            uk.gov.hmcts.reform.laubackend.eud.domain.EventType.MODIFY,
            clientId,
            principalId,
            currentUser,
            previousUser,
            eventTime
        );
    }

    static void seed(ServiceBusMessageHandler handler, List<IdamEvent> events) {
        for (IdamEvent event : events) {
            handler.handleMessage(event);
        }
    }

    static List<IdamEvent> emailAndNameChangeEvents(
        String clientId,
        String principalId,
        String userId
    ) {
        User emailPrev = user(
            "old.email@example.org",
            userId,
            "John",
            "Smith",
            List.of(CITIZEN_ROLE),
            AccountStatus.ACTIVE,
            RecordType.LIVE
        );
        User emailCurr = user(
            CHANGED_EMAIL,
            userId,
            "John",
            "Smith",
            List.of(CITIZEN_ROLE),
            AccountStatus.ACTIVE,
            RecordType.LIVE
        );
        IdamEvent emailEvent = modifyEvent(
            clientId,
            principalId,
            emailCurr,
            emailPrev,
            LocalDateTime.of(2024, 1, 1, 10, 15, 30)
        );

        User namePrev = user(
            CHANGED_EMAIL,
            userId,
            "John",
            "Smith",
            List.of(CITIZEN_ROLE),
            AccountStatus.ACTIVE,
            RecordType.LIVE
        );
        User nameCurr = user(
            CHANGED_EMAIL,
            userId,
            CHANGED_FIRST_NAME,
            CHANGED_LAST_NAME,
            List.of(CITIZEN_ROLE),
            AccountStatus.ACTIVE,
            RecordType.LIVE
        );
        IdamEvent nameEvent = modifyEvent(
            clientId,
            principalId,
            nameCurr,
            namePrev,
            LocalDateTime.of(2024, 1, 1, 10, 20, 30)
        );

        User rolePrev = user(
            CHANGED_EMAIL,
            userId,
            CHANGED_FIRST_NAME,
            CHANGED_LAST_NAME,
            List.of(CITIZEN_ROLE),
            AccountStatus.ACTIVE,
            RecordType.LIVE
        );
        User roleCurr = user(
            CHANGED_EMAIL,
            userId,
            CHANGED_FIRST_NAME,
            CHANGED_LAST_NAME,
            List.of(CHANGED_ROLE, CITIZEN_ROLE),
            AccountStatus.ACTIVE,
            RecordType.LIVE
        );
        IdamEvent roleEvent = modifyEvent(
            clientId,
            principalId,
            roleCurr,
            rolePrev,
            LocalDateTime.of(2024, 1, 1, 10, 25, 30)
        );

        User accountPrev = user(
            CHANGED_EMAIL,
            userId,
            CHANGED_FIRST_NAME,
            CHANGED_LAST_NAME,
            List.of(CHANGED_ROLE, CITIZEN_ROLE),
            AccountStatus.ACTIVE,
            RecordType.LIVE
        );
        User accountCurr = user(
            CHANGED_EMAIL,
            userId,
            CHANGED_FIRST_NAME,
            CHANGED_LAST_NAME,
            List.of(CHANGED_ROLE, CITIZEN_ROLE),
            AccountStatus.SUSPENDED,
            RecordType.LIVE
        );
        IdamEvent accountEvent = modifyEvent(
            clientId,
            principalId,
            accountCurr,
            accountPrev,
            LocalDateTime.of(2024, 1, 1, 10, 30, 30)
        );

        User recordPrev = user(
            CHANGED_EMAIL,
            userId,
            CHANGED_FIRST_NAME,
            CHANGED_LAST_NAME,
            List.of(CHANGED_ROLE, CITIZEN_ROLE),
            AccountStatus.SUSPENDED,
            RecordType.LIVE
        );
        User recordCurr = user(
            CHANGED_EMAIL,
            userId,
            CHANGED_FIRST_NAME,
            CHANGED_LAST_NAME,
            List.of(CHANGED_ROLE, CITIZEN_ROLE),
            AccountStatus.SUSPENDED,
            RecordType.ARCHIVED
        );
        IdamEvent recordEvent = modifyEvent(
            clientId,
            principalId,
            recordCurr,
            recordPrev,
            LocalDateTime.of(2024, 1, 1, 10, 35, 30)
        );

        return List.of(emailEvent, nameEvent, roleEvent, accountEvent, recordEvent);
    }
}
