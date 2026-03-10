package uk.gov.hmcts.reform.laubackend.eud.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Idam User GET Response")
public record IdamUserResponse(
    @Schema(description = "IdAM ID of the user.")
    @JsonAlias("id")
    String userId,

    @Schema(description = "Email address of the user.")
    String email,

    @Schema(description = "Account Status of user")
    String accountStatus,

    @Schema(description = "RecordType of user")
    String recordType,

    @Schema(description = "User's account creation timestamp in iso-8601-date-and-time-format.")
    @JsonAlias("createDate")
    String accountCreationDate,

    @Schema(description = "User's roles.")
    @JsonAlias("roleNames")
    List<String> roles,

    @Schema(description = "Forename")
    String forename,

    @Schema(description = "Surname")
    String surname,

    @Schema(description = "Display name")
    String displayName,

    @Schema(description = "SSO id")
    String ssoId,

    @Schema(description = "SSO provider")
    String ssoProvider,

    @Schema(description = "Last account modification timestamp")
    String lastModified,

    @Schema(description = "Account access locked timestamp")
    String accessLockedDate,

    @Schema(description = "Last user login timestamp")
    String lastLoginDate
) {
    public static IdamUserResponse empty() {
        return new IdamUserResponse(
            null, null, null,
            null, null, null,
            null, null, null,
            null, null, null,
            null, null
        );
    }
}
