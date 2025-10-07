package uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.helper;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.config.EnvConfig.IDAM_CLIENT_SECRET;
import static uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.utils.TestConstants.CLIENT_ID;
import static uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.utils.TestConstants.CREATE_USER_URL;
import static uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.utils.TestConstants.GRANT_TYPE;
import static uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.utils.TestConstants.S2S_NAME;
import static uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.utils.TestConstants.S2S_URL;
import static uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.utils.TestConstants.TOKEN_URL;
import static uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.utils.TestConstants.USER_SCOPE;

public class AuthorizationHeaderHelper {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(AuthorizationHeaderHelper.class);

    public String getAuthorizationToken() throws JSONException {
        Response response = RestAssured
            .given()
            .contentType("application/x-www-form-urlencoded; charset=utf-8")
            .formParam("grant_type", GRANT_TYPE)
            .formParam("scope", USER_SCOPE)
            .formParam("client_id", CLIENT_ID)
            .formParam("client_secret", IDAM_CLIENT_SECRET)
            .when()
            .post(TOKEN_URL);

        return "Bearer " + new JSONObject(response.getBody().asString())
            .getString("access_token");

    }

    public String getServiceToken() {

        LOGGER.info("s2sUrl lease url: {}", S2S_URL + "/lease");
        final Map<String, Object> params = of(
            "microservice", S2S_NAME
        );

        final Response response = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .baseUri(S2S_URL)
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(params)
            .when()
            .post("/lease")
            .andReturn();
        assertThat(response.getStatusCode()).isEqualTo(200);

        return "Bearer " + response
            .getBody()
            .asString();
    }

    public ExtractableResponse<Response> createUser() {
        try {
            return RestAssured.given()
                .header("Authorization", getAuthorizationToken())
                .header("Content-Type", "application/json")
                .body(makeUser())
                .when()
                .post(CREATE_USER_URL)
                .then()
                .statusCode(201)
                .extract();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String makeUser() {
        JSONObject makeUser = new JSONObject();
        try {
            makeUser.put("password","Test1234567");
            JSONObject user = new JSONObject();
            user.put("id", "functional1234567");
            user.put("email", "functional1234567@test.com");
            user.put("forename", "lautestEud");
            user.put("surname", "test");
            JSONArray roles = new JSONArray();
            roles.put("citizen");
            roles.put("caseworker");
            user.put("roleNames", roles);
            makeUser.put("user", user);
        } catch (JSONException je) {
            LOGGER.error(je.getMessage(), je);
        }

        return makeUser.toString();
    }
}
