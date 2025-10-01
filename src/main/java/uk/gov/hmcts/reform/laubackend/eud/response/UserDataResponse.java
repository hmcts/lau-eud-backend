package uk.gov.hmcts.reform.laubackend.eud.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Schema(description = "User Data GET Response")
public class UserDataResponse implements Serializable {

    public static final long serialVersionUID = 432973389L;

    @Schema(description = "IdAM ID of the user.")
    String id;
    @Schema(description = "Email address/username of the user.")
    String email;
    @Schema(description = "Account Status of user")
    String accountStatus;
    @Schema(description = "User's account creation timestamp in iso-8601-date-and-time-format.")
    String createDate;
    @Schema(description = "User's roles.")
    List<String> roleNames;

}
