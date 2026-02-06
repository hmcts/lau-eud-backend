package uk.gov.hmcts.reform.laubackend.eud.functionaltests.runner;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Title;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.laubackend.eud.functionaltests.model.UserUpdatesResponse;
import uk.gov.hmcts.reform.laubackend.eud.functionaltests.steps.UserUpdatesGetApiSteps;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@ExtendWith(SerenityJUnit5Extension.class)
public class UserUpdatesApiTest {

    @Steps
    UserUpdatesGetApiSteps userUpdatesGetApiSteps;

    @Test
    @Title("Assert response code of 200 for GET UserUpdates API with valid headers and valid request params")
    public void assertHttpSuccessResponseCodeForUserUpdatesApi() {
        String authServiceToken = userUpdatesGetApiSteps.givenAValidServiceTokenIsGenerated();
        String userId = userUpdatesGetApiSteps.createUserToPassAsParam();
        Map<String, String> queryParamMap = userUpdatesGetApiSteps
            .givenValidParamsAreSuppliedForGetUserUpdates(userId, "0", "10");
        ResponseEntity<UserUpdatesResponse> responseEntity = userUpdatesGetApiSteps
            .whenTheGetUserUpdatesIsInvokedWithTheGivenParams(
                authServiceToken,
                queryParamMap
            );

        userUpdatesGetApiSteps.thenTheGetUserUpdatesResponseParamsMatchesTheInput(queryParamMap,
                       responseEntity.getBody());
        userUpdatesGetApiSteps.thenASuccessResponseIsReturned(responseEntity);
        userUpdatesGetApiSteps.deleteTheUser();
    }

    @Test
    @Title("Assert response code of 403 for GET UserUpdates API with invalid ServiceAuthorization Token")
    public void assertResponseCodeOf403WithInvalidServiceAuthenticationTokenForUserUpdatesApi() {
        String invalidServiceToken = userUpdatesGetApiSteps.givenTheInvalidServiceTokenIsGenerated();
        String userId = userUpdatesGetApiSteps.createUserToPassAsParam();
        Map<String, String> queryParamMap = userUpdatesGetApiSteps
            .givenValidParamsAreSuppliedForGetUserUpdates(userId, "0", "10");
        Response response = userUpdatesGetApiSteps
            .whenTheGetUserUpdatesIsInvokedWithTheInvalidParams(
                invalidServiceToken,
                queryParamMap
            );
        userUpdatesGetApiSteps.thenBadResponseIsReturned(response, FORBIDDEN.value());
        userUpdatesGetApiSteps.deleteTheUser();
    }

    @Test
    @Title("Assert response code of 400 for GET UserUpdates API with empty userId")
    public void assertResponseCodeOf400WithInvalidParamsForUserUpdatesApi() {
        String authServiceToken = userUpdatesGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = userUpdatesGetApiSteps.givenEmptyParamsAreSuppliedForGetUserUpdates();
        Response response = userUpdatesGetApiSteps
            .whenTheGetUserUpdatesIsInvokedWithTheInvalidParams(
                authServiceToken,
                queryParamMap
            );
        userUpdatesGetApiSteps.thenBadResponseIsReturned(response, 400);
    }

    @Test
    @Title("Assert response code of 400 for GET UserUpdates API with userId greater than 64 characters")
    public void assertResponseCodeOf400ForUserIdGreaterThan64Characters() {
        String authServiceToken = userUpdatesGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = userUpdatesGetApiSteps.givenValidParamsAreSuppliedForGetUserUpdates(
            "6f86055-e978-4758-8e65-c0373cd77fc6f6f86055-e978-4758-8e65-c0373cd77fc6",
            "0",
            "10"
        );
        Response response = userUpdatesGetApiSteps.whenTheGetUserUpdatesIsInvokedWithTheInvalidParams(
            authServiceToken,
            queryParamMap
        );
        assertEquals(400, response.getStatusCode(),
                     "The assertion for GET UserUpdates API using more than 64 chars userId is not successful");
    }
}
