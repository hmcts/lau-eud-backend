package uk.gov.hmcts.reform.laubackend.eud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Data model for the User Data request")
public class UserDataGetRequestParams {
    @Size(max = 64, message = "UserId must not exceed 64 characters")
    private String userId;

    private String email;

    @AssertTrue(message = "At least one of userId or email must be provided")
    public boolean isAtLeastOneProvided() {
        return isNotEmpty(userId) || isNotEmpty(email);
    }
}


