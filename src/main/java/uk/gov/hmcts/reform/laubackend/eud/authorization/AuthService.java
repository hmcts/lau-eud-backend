package uk.gov.hmcts.reform.laubackend.eud.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;
import uk.gov.hmcts.reform.laubackend.eud.exceptions.InvalidServiceAuthorizationException;

@Component
public class AuthService {

    private final AuthTokenValidator authTokenValidator;

    @Autowired
    public AuthService(final AuthTokenValidator authTokenValidator) {
        this.authTokenValidator = authTokenValidator;
    }

    public String authenticateService(final String authHeader) {
        if (authHeader != null) {
            return authTokenValidator.getServiceName(authHeader);
        }
        throw new InvalidServiceAuthorizationException("Missing ServiceAuthorization header");
    }
}
