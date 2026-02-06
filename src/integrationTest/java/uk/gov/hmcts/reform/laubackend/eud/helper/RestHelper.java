package uk.gov.hmcts.reform.laubackend.eud.helper;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Map;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.gov.hmcts.reform.laubackend.eud.constants.CommonConstants.SERVICE_AUTHORISATION_HEADER;
import static uk.gov.hmcts.reform.laubackend.eud.helper.RestConstants.GOOD_TOKEN;

public class RestHelper {

    public Response getResponseWithoutHeader(final String path) {
        return RestAssured
            .given()
            .relaxedHTTPSValidation()
            .baseUri(path)
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .when()
            .get()
            .andReturn();
    }

    public Response getResponse(final String path,
                                final Map<String, String> queryParams) {
        return RestAssured
            .given()
            .relaxedHTTPSValidation()
            .baseUri(path)
            .queryParams(queryParams)
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + GOOD_TOKEN)
            .when()
            .get()
            .andReturn();
    }

    public Response getResponseWithServiceToken(final String path,
                                                final Map<String, String> queryParams,
                                                final String token) {
        return RestAssured
            .given()
            .relaxedHTTPSValidation()
            .baseUri(path)
            .queryParams(queryParams)
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .header(SERVICE_AUTHORISATION_HEADER, "Bearer " + token)
            .when()
            .get()
            .andReturn();
    }
}
