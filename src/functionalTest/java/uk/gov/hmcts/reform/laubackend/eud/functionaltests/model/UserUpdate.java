package uk.gov.hmcts.reform.laubackend.eud.functionaltests.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserUpdate(
    String eventName,
    @JsonProperty("eventType") String eventType,
    String value,
    String timestamp,
    String principalId,
    String previousValue
) {}
