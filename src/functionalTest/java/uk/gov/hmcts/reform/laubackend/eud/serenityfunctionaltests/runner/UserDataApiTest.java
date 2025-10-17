package uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Title;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.model.UserDataResponseVO;
import uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.steps.UserDataGetApiSteps;
import uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.utils.TestConstants;

import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RunWith(SerenityRunner.class)
public class UserDataApiTest {

    @Steps
    UserDataGetApiSteps userDataGetApiSteps;

    @Test
    @Title("Assert response code of 200 for GET UserData Api with valid headers and valid request params")
    public void assertHttpSuccessResponseCodeForCaseViewApi() throws Exception {
        String authServiceToken = userDataGetApiSteps.givenAValidServiceTokenIsGenerated();
        String userId = userDataGetApiSteps.createUserToPassAsParam();
        Map<String, String> queryParamMap = userDataGetApiSteps.givenValidParamsAreSuppliedForGetUserData(userId,"");
        Response response = userDataGetApiSteps.whenTheGetUserDataIsInvokedWithTheGivenParams(
            authServiceToken,
            queryParamMap
        );
        ObjectMapper objectMapper = new ObjectMapper();
        UserDataResponseVO userDataResponseVO = objectMapper.readValue(
            response.getBody().asString(),
            UserDataResponseVO.class
        );
        userDataGetApiSteps.thenTheGetUserDataResponseParamsMatchesTheInput(queryParamMap, userDataResponseVO);
        String successOrFailure = userDataGetApiSteps.thenASuccessResposeIsReturned(response);
        Assert.assertEquals("The assertion for GET UserData API response code 200 is not successful",
                            TestConstants.SUCCESS,successOrFailure);
        userDataGetApiSteps.deleteTheUser();
    }

    @Test
    @Title("Assert response code of 403 for GET UserData Api service with Invalid ServiceAuthorization Token")
    public void assertResponseCodeOf403WithInvalidServiceAuthenticationTokenForGetUserDataApi() {
        String invalidServiceToken = userDataGetApiSteps.givenTheInvalidServiceTokenIsGenerated();
        String userId = userDataGetApiSteps.createUserToPassAsParam();
        Map<String, String> queryParamMap = userDataGetApiSteps.givenValidParamsAreSuppliedForGetUserData(userId,"");
        Response response = userDataGetApiSteps.whenTheGetUserDataIsInvokedWithTheGivenParams(
            invalidServiceToken,
            queryParamMap
        );
        String successOrFailure = userDataGetApiSteps.thenBadResponseIsReturned(response, FORBIDDEN.value());
        Assert.assertEquals("Get UserData API response code 403 assertion is not successful",
                            TestConstants.SUCCESS,successOrFailure

        );
        userDataGetApiSteps.deleteTheUser();
    }

    @Test
    @Title("Assert response code of 400 for GET CaseActionApi with Empty Params")
    public void assertResponseCodeOf400WithInvalidParamsForCaseViewApi() {
        String authServiceToken = userDataGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = userDataGetApiSteps.givenEmptyParamsAreSuppliedForGetUserData();
        Response response = userDataGetApiSteps.whenTheGetUserDataIsInvokedWithTheGivenParams(
            authServiceToken,
            queryParamMap
        );
        String successOrFailure = userDataGetApiSteps.thenBadResponseIsReturned(response, 400);
        Assert.assertEquals("Get UserData API response code 400 assertion is not successful",
                            TestConstants.SUCCESS,successOrFailure);
    }

    @Test
    @Title("Assert response code of 404 for GET UserData API with valid headers and invalid request params")
    public void assertHttpSuccessResponse404ForInvalidUserId()  {
        String authServiceToken = userDataGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = userDataGetApiSteps.givenValidParamsAreSuppliedForGetUserData(
            "1122334455","");
        Response response = userDataGetApiSteps.whenTheGetUserDataIsInvokedWithTheGivenParams(
            authServiceToken,
            queryParamMap
        );
        Assert.assertEquals("The assertion for GET UserData API using userId response code 404 is not successful",
                            404,response.getStatusCode());
    }

    @Test
    @Title("Assert response code of 404 for GET UserData API with valid headers and invalid request params")
    public void assertHttpSuccessResponse404ForInvalidEmail() {
        String authServiceToken = userDataGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = userDataGetApiSteps.givenValidParamsAreSuppliedForGetUserData(
            "","randomtest@test.com");
        Response response = userDataGetApiSteps.whenTheGetUserDataIsInvokedWithTheGivenParams(
            authServiceToken,
            queryParamMap
        );
        Assert.assertEquals("The assertion for GET UserData API using email response code 404 is not successful",
                            404,response.getStatusCode());
    }

    @Test
    @Title("Assert response code of 400 for GET UserData API with valid headers and userId getter than 64 characters")
    public void assertHttpSuccessResponse400ForUserIdGreaterThan64Characters() {
        String authServiceToken = userDataGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = userDataGetApiSteps.givenValidParamsAreSuppliedForGetUserData(
                "6f86055-e978-4758-8e65-c0373cd77fc6f6f86055-e978-4758-8e65-c0373cd77fc6","");
        Response response = userDataGetApiSteps.whenTheGetUserDataIsInvokedWithTheGivenParams(
                authServiceToken,
                queryParamMap
        );
        Assert.assertEquals("The assertion for GET UserData API using more than 64 chars userId is not successful",
                400,response.getStatusCode());
    }

    @Test
    @Title("Assert response code of 400 for GET UserData API with valid headers and if mandatory param is missing")
    public void assertHttpSuccessResponse400ForMandatoryParamsMissing() {
        String authServiceToken = userDataGetApiSteps.givenAValidServiceTokenIsGenerated();
        Map<String, String> queryParamMap = userDataGetApiSteps.givenValidParamsAreSuppliedForGetUserData(
                "","");
        Response response = userDataGetApiSteps.whenTheGetUserDataIsInvokedWithTheGivenParams(
                authServiceToken,
                queryParamMap
        );
        Assert.assertEquals("The assertion for GET UserData API manadtory params mssing is not successful",
                400,response.getStatusCode());
    }
}
