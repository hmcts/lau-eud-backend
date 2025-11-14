package uk.gov.hmcts.reform.laubackend.eud.dto;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDataGetRequestParamsTest {

    private static final String USER_ID = "userId";
    private static final String EMAIL = "email@example.org";

    @Test
    void shouldCheckIfAtLeastOneOfParamsProvided() {
        assertThat(generateParams(USER_ID, null).isAtLeastOneProvided()).isTrue();
        assertThat(generateParams(USER_ID, "").isAtLeastOneProvided()).isTrue();
        assertThat(generateParams(null, EMAIL).isAtLeastOneProvided()).isTrue();
        assertThat(generateParams("  ", EMAIL).isAtLeastOneProvided()).isTrue();
        assertThat(generateParams(USER_ID, EMAIL).isAtLeastOneProvided()).isTrue();
        assertThat(generateParams(null, null).isAtLeastOneProvided()).isFalse();
        assertThat(generateParams("  ", "  ").isAtLeastOneProvided()).isFalse();
    }

    @Test
    void shouldReturnUserId() {
        assertThat(generateParams(USER_ID, null).getUserId()).isEqualTo(USER_ID);
        assertThat(generateParams(null, "").getUserId()).isNull();
        assertThat(generateParams("   ", "").getUserId()).isNull();
        assertThat(generateParams("   " + USER_ID + "  ", null).getUserId()).isEqualTo(USER_ID);
    }

    @Test
    void shouldReturnEmail() {
        assertThat(generateParams(null, EMAIL).getEmail()).isEqualTo(EMAIL);
        assertThat(generateParams(null, "").getEmail()).isNull();
        assertThat(generateParams(null, "  ").getEmail()).isNull();
        assertThat(generateParams("", "  " + EMAIL).getEmail()).isEqualTo(EMAIL);
    }

    private UserDataGetRequestParams generateParams(String userId, String email) {
        return new UserDataGetRequestParams(userId, email);
    }

}
