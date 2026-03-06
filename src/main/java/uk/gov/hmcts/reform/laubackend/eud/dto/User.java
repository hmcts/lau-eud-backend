package uk.gov.hmcts.reform.laubackend.eud.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import uk.gov.hmcts.reform.laubackend.eud.domain.AccountStatus;
import uk.gov.hmcts.reform.laubackend.eud.domain.RecordType;

import java.time.OffsetDateTime;
import java.util.List;

public record User(
    @Size(max = 256) String email,
    @NotBlank @Size(max = 64) String id,
    @Size(max = 256) String forename,
    @Size(max = 256) String surname,
    @Size(max = 256) String displayName,
    @Valid List<@Size(max = 128) String> roleNames,
    String ssoId,
    String ssoProvider,
    @NotNull AccountStatus accountStatus,
    @NotNull RecordType recordType,
    OffsetDateTime createDate,
    OffsetDateTime lastModified,
    OffsetDateTime accessLockedDate,
    OffsetDateTime lastLoginDate
) {}
