package uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDataResponseVO {

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("roles")
    private List<String> roles;

    @JsonProperty("accountStatus")
    private String accountStatus;

    @JsonProperty("accountCreationDate")
    private String accountCreationDate;
}
