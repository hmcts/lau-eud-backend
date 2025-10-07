package uk.gov.hmcts.reform.laubackend.eud.serenityfunctionaltests.utils;

@SuppressWarnings("PMD.TestClassWithoutTestCases")
public final class TestConstants {

    public static final String S2S_NAME = "lau_frontend";
    public static final String S2S_URL = "http://rpe-service-auth-provider-aat.service.core-compute-aat.internal/testing-support";

    // Authorization constants
    public static final String GRANT_TYPE = "client_credentials";
    public static final String REDIRECT_URI = "https://lau-eud.aat.platform.hmcts.net/oauth2/callback";
    public static final String SCOPE = "view-user";
    public static final String CLIENT_ID = "lau";
    public static final String TOKEN_URL = "https://idam-api.aat.platform.hmcts.net/o/token";
    public static final String USER_SCOPE = "roles profile";
    public static final String CREATE_USER_URL = "https://idam-testing-support-api.aat.platform.hmcts.net/test/idam/users";


    /*endPoint*/
    public static final String USER_DATA_ENDPOINT = "/userData";


    public static final String SUCCESS = "Success";

    private TestConstants() {

    }
}
