package uk.gov.hmcts.reform.laubackend.eud.utils;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.laubackend.eud.dto.UserDataGetRequestParams;
import uk.gov.hmcts.reform.laubackend.eud.exceptions.InvalidRequestException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.hmcts.reform.laubackend.eud.utils.NotEmptyInputParamsVerifier.verifyUserDataGetRequestParamsPresence;

public class NotEmptyInputParamsVerifierTest {

    @Test
    void shouldThrowExceptionWhenGetRequestParamsAreNullForUserData() {
        try {
            verifyUserDataGetRequestParamsPresence(new UserDataGetRequestParams(null,
                                                                          null));

            fail("The method should have thrown InvalidRequestException when all required params are not populated");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                .isEqualTo("UserId or EmailAddress parameter is required");
        }
    }

    @Test
    void shouldThrowExceptionWhenGetRequestParamsAreEmptyForUserData() {
        try {
            verifyUserDataGetRequestParamsPresence(new UserDataGetRequestParams("",
                                                                                ""));

            fail("The method should have thrown InvalidRequestException when all required params are not populated");
        } catch (final InvalidRequestException invalidRequestException) {
            assertThat(invalidRequestException.getMessage())
                .isEqualTo("UserId or EmailAddress parameter is required");
        }
    }

    @Test
    void shouldNotThrowExceptionWhenGetRequestParamsUserIdForUserData() {
        assertDoesNotThrow(() -> verifyUserDataGetRequestParamsPresence(
            new UserDataGetRequestParams("1",
            null)));
    }

    @Test
    void shouldNotThrowExceptionWhenGetRequestParamsEmailAddressForUserData() {
        assertDoesNotThrow(() -> verifyUserDataGetRequestParamsPresence(new
            UserDataGetRequestParams("", "test@test.com")));
    }
}
