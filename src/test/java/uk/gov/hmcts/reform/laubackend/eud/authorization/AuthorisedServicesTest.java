package uk.gov.hmcts.reform.laubackend.eud.authorization;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthorisedServicesTest {

    private static final String AUTHORISED_SERVICES_LIST = "authorisedServicesList";
    private static final String VALID_AUTHORISED_SERVICE = "lau_eud_backend";

    private static final String INVALID_AUTHORISED_SERVICE = "made_up";

    private static final String AUTHORISED_SUCCESS = "Should return true";
    private static final String AUTHORISED_FAILURE = "Should return false";

    @Autowired
    private AuthorisedServices authorisedServices;

    @BeforeEach
    void setUp() throws IllegalAccessException {
        authorisedServices = new AuthorisedServices();
        final List<String> authorizedServices = asList(VALID_AUTHORISED_SERVICE);

        FieldUtils.writeField(authorisedServices, AUTHORISED_SERVICES_LIST, authorizedServices, true);

    }

    @Test
    void testKnownAuthorisedServiceSuccess() {
        assertTrue(authorisedServices.hasService(VALID_AUTHORISED_SERVICE), AUTHORISED_SUCCESS);
    }

    @Test
    void testUnknownAuthorisedService() {
        assertFalse(authorisedServices.hasService(INVALID_AUTHORISED_SERVICE), AUTHORISED_FAILURE);
    }
}
