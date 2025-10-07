package uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Generated;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)

@Generated("jsonschema2pojo")
public class UserDataResponseVO {

    @JsonProperty("roleNames")
    private List<String> roleNames;
    @JsonProperty("id")
    private String id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("accountStatus")
    private String accountStatus;
    @JsonProperty("createDate")
    private String createDate;

    @JsonProperty("roleNames")
    public List<String> getRoleNames() {
        return roleNames;
    }

    @JsonProperty("roleNames")
    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void seId(String id) {
        this.id = id;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }
}
