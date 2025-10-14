package uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.steps;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.model.UserDataResponseVO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.utils.TestConstants.SUCCESS;
import static uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.utils.TestConstants.USER_DATA_ENDPOINT;

public class UserDataGetApiSteps extends BaseSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataGetApiSteps.class);

    @Step("When valid params are supplied for Get UserData API")
    public Map<String, String> givenValidParamsAreSuppliedForGetUserData(String userId, String email) {
        Map<String, String> queryParamMap = new ConcurrentHashMap<>();
        queryParamMap.put("userId", userId);
        queryParamMap.put("email", email);
        return queryParamMap;
    }

    @Step("When the UserData GET service is invoked with the valid params")
    public Response whenTheGetUserDataIsInvokedWithTheGivenParams(String serviceToken,
                                                                           Map<String, String> queryParamMap) {
        return performGetOperation(USER_DATA_ENDPOINT,
                                   null, queryParamMap, serviceToken);
    }

    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    @Step("Then the GET CaseAction response params match the input")
    public String thenTheGetUserDataResponseParamsMatchesTheInput(Map<String, String> inputQueryParamMap,
                                                                    UserDataResponseVO userDataResponseVO) {

        for (String queryParam : inputQueryParamMap.keySet()) {

            if ("userId".equals(queryParam) && !isEmpty(inputQueryParamMap.get(queryParam))) {
                String userId = userDataResponseVO.getUserId();
                Assert.assertEquals(
                    "User Id is missing in the response",
                    inputQueryParamMap.get(queryParam), userId
                );
            } else if ("email".equals(queryParam) && !isEmpty(inputQueryParamMap.get(queryParam))) {
                String email = userDataResponseVO.getEmail();
                Assert.assertEquals(
                    "email is missing in the response",
                    inputQueryParamMap.get(queryParam), email
                );

            }
        }
        return SUCCESS;
    }

    @Step("Given empty params values are supplied for the GET UserData API")
    public Map<String, String> givenEmptyParamsAreSuppliedForGetUserData() {
        Map<String, String> queryParamMap = new ConcurrentHashMap<>();
        queryParamMap.put("userId", "");
        queryParamMap.put("email", "");
        return queryParamMap;
    }

    @Step("Given the invalid service authorization token is generated")
    public String givenTheInvalidServiceTokenIsGenerated() {
        String authServiceToken = givenAValidServiceTokenIsGenerated();
        return authServiceToken + "abc";
    }

    @Step("Then bad response is returned")
    public String thenBadResponseIsReturned(Response response, int expectedStatusCode) {
        Assert.assertEquals(
            "Response status code is not " + expectedStatusCode + ", but it is " + response.getStatusCode(),
            expectedStatusCode,response.statusCode()
        );
        return SUCCESS;
    }

    @Step("Then a success response is returned")
    public String thenASuccessResposeIsReturned(Response response) {
        Assert.assertTrue(
            "Response status code is not 200, but it is " + response.getStatusCode(),
            response.statusCode() == 200 || response.statusCode() == 201
        );
        return SUCCESS;
    }

    @Step("Create a user to pass as param")
    public String createUserToPassAsParam() {
        String userId = createUser();
        Assert.assertNotNull("User Id is null",userId);
        return userId;
    }

    @Step("Delete the user after test")
    public void deleteTheUser() {
        int statusCode =  deleteUser();
        Assert.assertEquals(204,statusCode);
    }
}
