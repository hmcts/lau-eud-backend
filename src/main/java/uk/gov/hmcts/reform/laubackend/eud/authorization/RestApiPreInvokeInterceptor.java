package uk.gov.hmcts.reform.laubackend.eud.authorization;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import uk.gov.hmcts.reform.laubackend.eud.exceptions.InvalidServiceAuthorizationException;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

@Slf4j
public class RestApiPreInvokeInterceptor implements HandlerInterceptor {

    @Autowired
    private ServiceAuthorizationAuthenticator serviceAuthorizationAuthenticator;

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws IOException {

        try {
            serviceAuthorizationAuthenticator.authorizeServiceToken(request);

        } catch (final InvalidServiceAuthorizationException exception) {
            log.error(
                "Service authorization token failed due to error - {}",
                exception.getMessage(),
                exception
            );
            response.sendError(SC_FORBIDDEN, exception.getMessage());

            return false;

        }
        return true;
    }
}
