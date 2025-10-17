package uk.gov.hmcts.reform.laubackend.eud.authorization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;
import uk.gov.hmcts.reform.laubackend.eud.exceptions.InvalidServiceAuthorizationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthServiceTest {

    private static final String REFORM_SCAN_BLOB_ROUTER_SERVICE_AUTH = "DFJSDFSDFSDFSDFSDSFS";
    private static final String REFORM_SCAN_BLOB_ROUTER_SERVICE_NAME = "lau_eud_backend";

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthTokenValidator authTokenValidator;

    @BeforeEach
    void setUp() {
        this.authService = new AuthService(authTokenValidator);
    }

    @Test
    void testShouldGetServiceAuthName() {
        when(authTokenValidator.getServiceName(REFORM_SCAN_BLOB_ROUTER_SERVICE_AUTH))
            .thenReturn(REFORM_SCAN_BLOB_ROUTER_SERVICE_NAME);

        final String actualServiceName = authService.authenticateService(REFORM_SCAN_BLOB_ROUTER_SERVICE_AUTH);

        assertNotNull(actualServiceName, "Should be not null");
        assertEquals(REFORM_SCAN_BLOB_ROUTER_SERVICE_NAME,
                     actualServiceName, "Should return authenticated service name");
    }

    @Test
    void testShouldErrorIfServiceNotAuthenticated() {
        try {
            authService.authenticateService(null);
            fail("The method should have thrown InvalidAuthenticationException");
        } catch (InvalidServiceAuthorizationException iae) {
            assertThat(iae.getMessage()).isEqualTo("Missing ServiceAuthorization header");
        }
    }
}
