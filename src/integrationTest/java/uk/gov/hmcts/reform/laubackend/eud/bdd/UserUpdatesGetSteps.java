package uk.gov.hmcts.reform.laubackend.eud.bdd;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.reform.laubackend.eud.repository.IdamUserChangeAuditRepository;
import uk.gov.hmcts.reform.laubackend.eud.service.ServiceBusMessageHandler;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class UserUpdatesGetSteps extends AbstractSteps {

    private static final String USER_ID = "13e31622-edea-493c-8240-9b780c9d6111";
    private static final String PRINCIPAL_ID = "principal-123";

    @Autowired
    private IdamUserChangeAuditRepository repository;

    @Autowired
    private ServiceBusMessageHandler handler;

    private String userUpdatesResponseBody;

    @Before
    public void setUpCommon() {
        setupStub();
    }

    @Before("@userUpdates")
    public void setUp() {
        repository.deleteAll();
        UserEventTestData.seed(
            handler,
            UserEventTestData.emailAndNameChangeEvents("lau-client", PRINCIPAL_ID, USER_ID)
        );
    }

    @When("I GET {string} using query param userId {string} with page {string} and size {string}")
    public void searchUserUpdates(final String path, String userId, String page, String size) {
        final Response response = restHelper.getResponse(baseUrl() + path,
                                                         Map.of("userId", userId, "page", page, "size", size));
        userUpdatesResponseBody = response.getBody().asString();
    }

    @Then("a userUpdates response body is returned for userId {string}")
    public void assertResponse(final String userId) {
        JsonPath json = new JsonPath(userUpdatesResponseBody);
        List<Map<String, Object>> content = json.getList("content");
        assertThat(content).isNotNull();
        assertThat(content.size()).isGreaterThanOrEqualTo(1);

        assertThat(json.getInt("page")).isEqualTo(0);
        assertThat(json.getInt("size")).isEqualTo(10);
        assertThat(json.getInt("totalElements")).isGreaterThanOrEqualTo(1);
        assertThat(json.getInt("totalPages")).isGreaterThanOrEqualTo(1);
        assertThat(userId).isEqualTo(USER_ID);
    }

    @Then("userUpdates totalElements is {int}")
    public void assertTotalElements(int expected) {
        JsonPath json = new JsonPath(userUpdatesResponseBody);
        assertThat(json.getInt("totalElements")).isEqualTo(expected);
        assertThat(json.getInt("totalPages")).isGreaterThanOrEqualTo(1);
    }

    @Then("database has {int} user update records")
    public void assertDatabaseCount(int expected) {
        assertThat(repository.count()).isEqualTo(expected);
    }

    @Then("userUpdates changed values are:")
    public void assertChangedValues(DataTable table) {
        assertUserUpdatesMatch(table, "value");
    }

    @Then("userUpdates previous values are:")
    public void assertPreviousValues(DataTable table) {
        assertUserUpdatesMatch(table, "previousValue");
    }

    private void assertUserUpdatesMatch(DataTable table, String valueKey) {
        JsonPath json = new JsonPath(userUpdatesResponseBody);
        Map<String, String> expected = table.asMaps().get(0);
        List<Map<String, Object>> content = json.getList("content");
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            String eventName = entry.getKey();
            String expectedValue = entry.getValue();
            assertThat(content)
                .anySatisfy(change -> {
                    assertThat(change.get("eventName")).isEqualTo(eventName);
                    assertThat(change.get(valueKey)).isEqualTo(expectedValue);
                });
        }
    }

}
