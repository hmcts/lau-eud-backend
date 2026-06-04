package uk.gov.hmcts.reform.laubackend.eud.functionaltests.steps;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.laubackend.eud.functionaltests.model.UserUpdatesResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static uk.gov.hmcts.reform.laubackend.eud.functionaltests.utils.TestConstants.SUCCESS;
import static uk.gov.hmcts.reform.laubackend.eud.functionaltests.utils.TestConstants.USER_UPDATES_ENDPOINT;

public class UserUpdatesGetApiSteps extends BaseSteps {

    @Step("When valid params are supplied for Get UserUpdates API")
    public Map<String, String> givenValidParamsAreSuppliedForGetUserUpdates(String userId, String page, String size) {
        Map<String, String> queryParamMap = new ConcurrentHashMap<>();
        queryParamMap.put("userId", userId);
        queryParamMap.put("page", page);
        queryParamMap.put("size", size);
        return queryParamMap;
    }

    @Step("When the UserUpdates GET service is invoked with the valid params")
    public ResponseEntity<UserUpdatesResponse> whenTheGetUserUpdatesIsInvokedWithTheGivenParams(
        String serviceToken,Map<String, String> queryParamMap) {
        return performGetUserUpdatesOperation(USER_UPDATES_ENDPOINT,
                                              null, queryParamMap, serviceToken);
    }

    @Step("When the UserUpdates GET service is invoked with the invalid params")
    public Response whenTheGetUserUpdatesIsInvokedWithTheInvalidParams(String serviceToken,
                                                                       Map<String, String> queryParamMap) {
        return performGetOperationWithInvalidParams(USER_UPDATES_ENDPOINT,
                                                     null, queryParamMap, serviceToken);
    }

    @Step("Then the GET UserUpdates response params match the input")
    public String thenTheGetUserUpdatesResponseParamsMatchesTheInput(
        Map<String, String> inputQueryParamMap, UserUpdatesResponse actualResponse) {

        if (actualResponse == null || actualResponse.content() == null) {
            fail("UserUpdates response or response.content is null");
        }

        String queryPage = inputQueryParamMap.get("page");
        String querySize = inputQueryParamMap.get("size");
        if (!isEmpty(queryPage)) {
            assertThat(Integer.parseInt(queryPage))
                .as("Page is not matching in the response")
                .isEqualTo(actualResponse.page());
        }
        if (!isEmpty(querySize)) {
            assertThat(Integer.parseInt(querySize))
                .as("Size is not matching in the response")
                .isEqualTo(actualResponse.size());
        }

        return SUCCESS;
    }

    @Step("Given empty params values are supplied for the GET UserUpdates API")
    public Map<String, String> givenEmptyParamsAreSuppliedForGetUserUpdates() {
        Map<String, String> queryParamMap = new ConcurrentHashMap<>();
        queryParamMap.put("userId", "");
        queryParamMap.put("page", "0");
        queryParamMap.put("size", "10");
        return queryParamMap;
    }

    @Step("Given the invalid service authorization token is generated")
    public String givenTheInvalidServiceTokenIsGenerated() {
        String authServiceToken = givenAValidServiceTokenIsGenerated();
        return authServiceToken + "abc";
    }

    @Step("Then bad response is returned")
    public void thenBadResponseIsReturned(Response response, int expectedStatusCode) {
        Assertions.assertEquals(
            expectedStatusCode,
            response.getStatusCode(),
            "Response status code is not " + expectedStatusCode + ", but it is " + response.getStatusCode()
        );
    }

    @Step("Then a success response is returned")
    public void thenASuccessResponseIsReturned(ResponseEntity<UserUpdatesResponse> responseEntity) {
        Assertions.assertTrue(
            responseEntity.getStatusCode().value() == 200 || responseEntity.getStatusCode().value() == 201,
            "Response status code is not 200 or 201, but it is " + responseEntity.getStatusCode().value()
        );
    }

    @Step("Create a user to pass as param")
    public String createUserToPassAsParam() {
        String userId = createUser();
        Assertions.assertNotNull(userId, "User Id is null");
        return userId;
    }

    @Step("Delete the user after test")
    public void deleteTheUser() {
        int statusCode = deleteUser();
        Assertions.assertEquals(204, statusCode);
    }
}
