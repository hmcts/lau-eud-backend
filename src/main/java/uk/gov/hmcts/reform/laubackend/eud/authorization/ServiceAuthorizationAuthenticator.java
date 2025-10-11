package uk.gov.hmcts.reform.laubackend.eud.authorization;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.eud.exceptions.InvalidServiceAuthorizationException;

import static uk.gov.hmcts.reform.laubackend.eud.constants.CommonConstants.SERVICE_AUTHORISATION_HEADER;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings({"PMD.PreserveStackTrace","PMD.ExceptionAsFlowControl"})
public class ServiceAuthorizationAuthenticator {

    private final AuthService authService;

    private final AuthorisedServices authorisedServices;

    public void authorizeServiceToken(HttpServletRequest httpServletRequest) {
        try {
            String serviceAuthHeader = httpServletRequest.getHeader(SERVICE_AUTHORISATION_HEADER);

            final String serviceName = String.valueOf(authService.authenticateService(serviceAuthHeader));
            if (!authorisedServices.hasService(serviceName)) {
                log.info("Service {} has NOT been authorised!", serviceName);
                throw new InvalidServiceAuthorizationException("Unable to authenticate service name.");
            }
        } catch (final Exception exception) {
            throw new InvalidServiceAuthorizationException(exception.getMessage());
        }
    }
}
