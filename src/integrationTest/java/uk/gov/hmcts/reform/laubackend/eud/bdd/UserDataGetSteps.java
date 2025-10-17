package uk.gov.hmcts.reform.laubackend.eud.bdd;

import com.google.gson.Gson;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import uk.gov.hmcts.reform.laubackend.eud.response.UserDataResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserDataGetSteps extends AbstractSteps {

    private final Gson jsonReader = new Gson();
    private String userDataGetResponseBody;

    @Before
    public void setUp() {
        setupStub();
    }


    @When("And I GET {string} using query param either userId {string} or email {string}")
    public void searchUser(final String path, String userId, String email) {
        final Response response = restHelper.getResponse(baseUrl() + path,
                                                         Map.of("userId", userId, "email", email));
        userDataGetResponseBody = response.getBody().asString();
    }

    @Then("a single userData response body is returned for for param userId {string} or email {string}")
    public void assertResponse(final String userId, String email) {
        final UserDataResponse userDataResponse = jsonReader
            .fromJson(userDataGetResponseBody, UserDataResponse.class);
        assertObject(userDataResponse);
    }

    private void assertObject(final UserDataResponse userDataResponse) {
        assertThat(userDataResponse).isNotNull();
        assertThat(userDataResponse.getUserId())
            .isEqualTo("13e31622-edea-493c-8240-9b780c9d6111");
        assertThat(userDataResponse.getEmail())
            .isEqualTo("john111.smith111@example.org");
        assertThat(userDataResponse.getAccountStatus())
            .isEqualTo("ACTIVE");
        String[] roles = {"citizen","caseworker-civil"};
        assertThat(userDataResponse.getRoles())
            .isEqualTo(List.of(roles));
        assertThat(userDataResponse.getAccountCreationDate())
            .isEqualTo("2023-06-21T13:28:40.966619Z");
    }
}
