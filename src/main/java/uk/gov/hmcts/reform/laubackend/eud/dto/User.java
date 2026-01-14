package uk.gov.hmcts.reform.laubackend.eud.dto;

import uk.gov.hmcts.reform.laubackend.eud.domain.AccountStatus;
import uk.gov.hmcts.reform.laubackend.eud.domain.RecordType;

import java.time.ZonedDateTime;
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
    ZonedDateTime createDate,
    ZonedDateTime lastModified,
    ZonedDateTime accessLockedDate,
    ZonedDateTime lastLoginDate
) {}
