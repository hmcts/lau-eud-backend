package uk.gov.hmcts.reform.laubackend.eud.bdd;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.reform.laubackend.eud.domain.EventType;
import uk.gov.hmcts.reform.laubackend.eud.domain.IdamUserChangeAudit;
import uk.gov.hmcts.reform.laubackend.eud.repository.IdamUserChangeAuditRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class UserUpdatesGetSteps extends AbstractSteps {

    private static final String USER_ID = "13e31622-edea-493c-8240-9b780c9d6111";
    private static final String PRINCIPAL_ID = "principal-123";
    private static final OffsetDateTime EVENT_TIMESTAMP = OffsetDateTime.parse("2024-01-01T10:15:30Z");

    @Autowired
    private IdamUserChangeAuditRepository repository;

    private String userUpdatesResponseBody;

    @Before("@userUpdates")
    public void setUp() {
        setupStub();
        repository.deleteAll();
        repository.save(IdamUserChangeAudit.builder()
                            .userId(USER_ID)
                            .principalUserId(PRINCIPAL_ID)
                            .eventType(EventType.ADD)
                            .eventName("account-status")
                            .eventValue("ACTIVE")
                            .previousEventValue("PENDING")
                            .eventTimestamp(EVENT_TIMESTAMP)
                            .build());
    }

    @When("And I GET {string} using query param userId {string} with page {string} and size {string}")
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
        assertThat(content.size()).isEqualTo(1);

        assertThat(json.getString("content[0].eventName")).isEqualTo("account-status");
        assertThat(json.getString("content[0].updateType")).isEqualTo("ADD");
        assertThat(json.getString("content[0].value")).isEqualTo("ACTIVE");
        assertThat(json.getString("content[0].previousValue")).isEqualTo("PENDING");
        assertThat(json.getString("content[0].principalId")).isEqualTo(PRINCIPAL_ID);
        assertThat(json.getString("content[0].timestamp")).startsWith("2024-01-01T10:15:30");

        assertThat(json.getInt("page")).isEqualTo(0);
        assertThat(json.getInt("size")).isEqualTo(10);
        assertThat(json.getInt("totalElements")).isEqualTo(1);
        assertThat(json.getInt("totalPages")).isEqualTo(1);
        assertThat(userId).isEqualTo(USER_ID);
    }

}
