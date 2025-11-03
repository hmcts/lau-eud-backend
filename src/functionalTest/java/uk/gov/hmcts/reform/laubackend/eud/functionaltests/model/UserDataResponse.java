package uk.gov.hmcts.reform.laubackend.eud.functionaltests.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.hmcts.reform.laubackend.eud.response.ContactInformationResponse;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDataResponse(
    @JsonProperty("userId") String userId,
    @JsonProperty("email") String email,
    @JsonProperty("accountStatus") String accountStatus,
    @JsonProperty("recordType") String recordType,
    @JsonProperty("accountCreationDate") String accountCreationDate,
    @JsonProperty("roles") List<String> roles,
    @JsonProperty("organisationalAddress") List<ContactInformationResponse> organisationalAddress,
    @JsonProperty("meta") Map<String, Map<String, Integer>> meta
) {}
