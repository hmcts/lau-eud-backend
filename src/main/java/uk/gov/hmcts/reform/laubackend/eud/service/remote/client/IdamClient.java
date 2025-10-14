package uk.gov.hmcts.reform.laubackend.eud.service.remote.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.hmcts.reform.idam.client.models.TokenResponse;
import uk.gov.hmcts.reform.laubackend.eud.constants.CommonConstants;
import uk.gov.hmcts.reform.laubackend.eud.response.UserDataResponse;

@FeignClient(name = "idamClient", url = "${idam.api.url}")
@SuppressWarnings({"PMD.UseObjectForClearerAPI"})
public interface IdamClient {
    String CONTENT_TYPE = "application/json";

    @GetMapping(value = CommonConstants.USER_DATA_BY_USERID_PATH + "{userId}",
        consumes = CONTENT_TYPE,
        produces = CONTENT_TYPE)
    UserDataResponse getUserDataByUserId(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable(name = "userId") String userId
    );

    @GetMapping(value = CommonConstants.USER_DATA_BY_EMAIL_PATH + "{email}",
        consumes = CONTENT_TYPE,
        produces = CONTENT_TYPE)
    UserDataResponse getUserDataByEmail(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable(name = "email") String email
    );

    @PostMapping(
        value = "/o/token",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    TokenResponse getToken(
        @RequestParam("client_id") String clientId,
        @RequestParam("client_secret") String clientSecret,
        @RequestParam("redirect_uri") String redirectUri,
        @RequestParam("grant_type") String grantType,
        @RequestParam("scope") String scope
    );

}
