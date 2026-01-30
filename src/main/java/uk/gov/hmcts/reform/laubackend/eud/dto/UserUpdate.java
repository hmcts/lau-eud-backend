package uk.gov.hmcts.reform.laubackend.eud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.hmcts.reform.laubackend.eud.domain.EventType;
import uk.gov.hmcts.reform.laubackend.eud.domain.IdamUserChangeAudit;

import java.time.OffsetDateTime;

public record UserUpdate(
    String eventName,
    @JsonProperty("eventType") EventType updateType,
    String value,
    OffsetDateTime timestamp,
    String principalId,
    String previousValue
) {
    public static UserUpdate from(IdamUserChangeAudit idamUserChangeAudit) {
        return new UserUpdate(
            idamUserChangeAudit.getEventName(),
            idamUserChangeAudit.getEventType(),
            idamUserChangeAudit.getEventValue(),
            idamUserChangeAudit.getEventTimestamp(),
            idamUserChangeAudit.getPrincipalUserId(),
            idamUserChangeAudit.getPreviousEventValue()
            );
    }
}
