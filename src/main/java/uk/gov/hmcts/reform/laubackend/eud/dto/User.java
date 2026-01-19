package uk.gov.hmcts.reform.laubackend.eud.dto;

import uk.gov.hmcts.reform.laubackend.eud.domain.AccountStatus;
import uk.gov.hmcts.reform.laubackend.eud.domain.RecordType;

import java.time.OffsetDateTime;
import java.util.List;

public record User(
    String email,
    String id,
    String forename,
    String surname,
    String displayName,
    List<String> roleNames,
    String ssoId,
    String ssoProvider,
    AccountStatus accountStatus,
    RecordType recordType,
    OffsetDateTime createDate,
    OffsetDateTime lastModified,
    OffsetDateTime accessLockedDate,
    OffsetDateTime lastLoginDate
) {}
