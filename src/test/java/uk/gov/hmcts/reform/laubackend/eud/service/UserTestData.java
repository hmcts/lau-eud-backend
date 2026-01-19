package uk.gov.hmcts.reform.laubackend.eud.service;

import uk.gov.hmcts.reform.laubackend.eud.domain.AccountStatus;
import uk.gov.hmcts.reform.laubackend.eud.domain.RecordType;
import uk.gov.hmcts.reform.laubackend.eud.dto.User;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class UserTestData {
    private String email = "Boyd_Schuster59@iud.nightlydevno1189.local";
    private String id = "60e97f57-7d2e-434a-8bcc-285fbb62354a";
    private String forename = "Boyd";
    private String surname = "Schuster";
    private String displayName = "Boyd Schuster";
    private List<String> roleNames = List.of("iud-test-worker");
    private String ssoId = null;
    private String ssoProvider = null;
    private AccountStatus accountStatus = AccountStatus.ACTIVE;
    private RecordType recordType = RecordType.LIVE;
    private OffsetDateTime createDate =
        Instant.parse("2026-01-09T12:26:24.079487486Z").atOffset(ZoneOffset.UTC);
    private OffsetDateTime lastModified =
        Instant.parse("2026-01-09T12:26:25.096041892Z").atOffset(ZoneOffset.UTC);
    private OffsetDateTime accessLockedDate = null;
    private OffsetDateTime lastLoginDate = null;

    UserTestData withAccountStatus(AccountStatus status) {
        this.accountStatus = status;
        return this;
    }

    UserTestData withRoleNames(List<String> roles) {
        this.roleNames = roles;
        return this;
    }

    UserTestData withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    UserTestData withLastModified(OffsetDateTime lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    User build() {
        return new User(
            email,
            id,
            forename,
            surname,
            displayName,
            roleNames,
            ssoId,
            ssoProvider,
            accountStatus,
            recordType,
            createDate,
            lastModified,
            accessLockedDate,
            lastLoginDate
        );
    }
}
