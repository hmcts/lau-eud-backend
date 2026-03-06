package uk.gov.hmcts.reform.laubackend.eud.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import uk.gov.hmcts.reform.laubackend.eud.domain.EventType;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public record IdamEvent(
    @NotNull EventType eventType,
    String clientId,
    String principalId,
    @NotNull @Valid User user,
    @Valid User previousUser,
    @NotNull LocalDateTime eventDateTime
) {
    public OffsetDateTime eventDateTimeUtc() {
        return eventDateTime.atOffset(ZoneOffset.UTC);
    }
}
