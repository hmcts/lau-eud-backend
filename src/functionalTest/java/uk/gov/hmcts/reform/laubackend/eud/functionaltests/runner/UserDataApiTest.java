package uk.gov.hmcts.reform.laubackend.eud.functionaltests.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Title;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.laubackend.eud.functionaltests.model.UserDataResponse;
import uk.gov.hmcts.reform.laubackend.eud.functionaltests.steps.UserDataGetApiSteps;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@ExtendWith(SerenityJUnit5Extension.class)
public class UserDataApiTest {

    @Steps
    UserDataGetApiSteps userDataGetApiSteps;

    @Test
    @Title("Assert response code of 200 for GET UserData Api with valid headers and valid request params")
    public void assertHttpSuccessResponseCodeForCaseViewApi() throws Exception {
        String authServiceToken = userDataGetApiSteps.givenAValidServiceTokenIsGenerated();
        String userId = userDataGetApiSteps.createUserToPassAsParam();
        Map<String, String> queryParamMap = userDataGetApiSteps
            .givenValidParamsAreSuppliedForGetUserData(userId,"");
        ResponseEntity<UserDataResponse> responseEntity = userDataGetApiSteps
            .whenTheGetUserDataIsInvokedWithTheGivenParams(
            authServiceToken,
            queryParamMap
        );

        ObjectMapper objectMapper = new ObjectMapper();
        UserDataResponse actualResponse = objectMapper.convertValue(
            responseEntity.getBody(),
            UserDataResponse.class
        );

        userDataGetApiSteps.thenTheGetUserDataResponseParamsMatchesTheInput(queryParamMap, actualResponse);
        userDataGetApiSteps.thenASuccessResposeIsReturned(responseEntity);
        userDataGetApiSteps.deleteTheUser();
    }

    @Test
    @Title("Assert response code of 403 for GET UserData Api service with Invalid ServiceAuthorization Token")
    public void assertResponseCodeOf403WithInvalidServiceAuthenticationTokenForGetUserDataApi() {
        String invalidServiceToken = userDataGetApiSteps.givenTheInvalidServiceTokenIsGenerated();
        String userId = userDataGetApiSteps.createUserToPassAsParam();
        Map<String, String> queryParamMap = userDataGetApiSteps
            .givenValidParamsAreSuppliedForGetUserData(userId,"");
        Response response = userDataGetApiSteps
            .whenTheGetUserDataIsInvokedWithTheInvalidParams(
            invalidServiceToken,
            queryParamMap
        );
        userDataGetApiSteps.thenBadResponseIsReturned(response, FORBIDDEN.value());
        userDataGetApiSteps.deleteTheUser();
    }

    @Test
    @Title("Assert response code of 400 for GET CaseActionApi with Empty Params")
    public void assertResponseCodeOf400WithInvalidParamsForCaseViewApi() {
        String authServiceToken = userDataGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = userDataGetApiSteps.givenEmptyParamsAreSuppliedForGetUserData();
        Response response = userDataGetApiSteps
            .whenTheGetUserDataIsInvokedWithTheInvalidParams(
            authServiceToken,
            queryParamMap
        );
        userDataGetApiSteps.thenBadResponseIsReturned(response, 400);
    }

    @Test
    @Title("Assert response code of 404 for GET UserData API with valid headers and invalid request params")
    public void assertHttpSuccessResponse404ForInvalidUserId()  {
        String authServiceToken = userDataGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = userDataGetApiSteps.givenValidParamsAreSuppliedForGetUserData(
            "1122334455","");
        ResponseEntity<UserDataResponse> responseEntity = userDataGetApiSteps
            .whenTheGetUserDataIsInvokedWithTheGivenParams(
            authServiceToken,
            queryParamMap
        );
        UserDataResponse userDataResponse = responseEntity.getBody();
        Integer status = userDataResponse.meta().get("idam").get("responseCode");
        assertEquals(404, status.intValue(),
                     "The assertion for GET UserData API using userId response code 404 is not successful");
    }

    @Test
    @Title("Assert response code of 404 for GET UserData API with valid headers and invalid request params")
    public void assertHttpSuccessResponse404ForInvalidEmail() {
        String authServiceToken = userDataGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = userDataGetApiSteps.givenValidParamsAreSuppliedForGetUserData(
            "","randomtest@test.com");
        ResponseEntity<UserDataResponse> responseEntity = userDataGetApiSteps
            .whenTheGetUserDataIsInvokedWithTheGivenParams(
            authServiceToken,
            queryParamMap
        );
        UserDataResponse userDataResponse = responseEntity.getBody();
        Integer status = userDataResponse.meta().get("idam").get("responseCode");
        assertEquals(404, status.intValue(),
                     "The assertion for GET UserData API using email response code 404 is not successful");
    }

    @Test
    @Title("Assert response code of 400 for GET UserData API with valid headers and userId getter than 64 characters")
    public void assertHttpSuccessResponse400ForUserIdGreaterThan64Characters() {
        String authServiceToken = userDataGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = userDataGetApiSteps.givenValidParamsAreSuppliedForGetUserData(
                "6f86055-e978-4758-8e65-c0373cd77fc6f6f86055-e978-4758-8e65-c0373cd77fc6","");
        Response response = userDataGetApiSteps.whenTheGetUserDataIsInvokedWithTheInvalidParams(
                authServiceToken,
                queryParamMap
        );
        assertEquals(400, response.getStatusCode(),
                     "The assertion for GET UserData API using more than 64 chars userId is not successful");
    }

    @Test
    @Title("Assert response code of 400 for GET UserData API with valid headers and if mandatory param is missing")
    public void assertHttpSuccessResponse400ForMandatoryParamsMissing() {
        String authServiceToken = userDataGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = userDataGetApiSteps.givenValidParamsAreSuppliedForGetUserData(
                "","");
        Response response = userDataGetApiSteps.whenTheGetUserDataIsInvokedWithTheInvalidParams(
                authServiceToken,
                queryParamMap
        );
        assertEquals(400, response.getStatusCode(),
                     "The assertion for GET UserData API manadtory params mssing is not successful");
    }
}
