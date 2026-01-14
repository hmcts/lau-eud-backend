package uk.gov.hmcts.reform.laubackend.eud.dto;

import uk.gov.hmcts.reform.laubackend.eud.domain.EventType;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public record IdamEvent(
    EventType eventType,
    String clientId,
    String principalId,
    User user,
    User previousUser,
    LocalDateTime eventDateTime
) {
    public OffsetDateTime eventDateTimeUtc() {
        return eventDateTime.atOffset(ZoneOffset.UTC);
    }
}
