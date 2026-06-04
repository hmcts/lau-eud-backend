package uk.gov.hmcts.reform.laubackend.eud.authorization;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestApiPreInvokeInterceptor implements HandlerInterceptor {

    private final ObjectProvider<ServiceAuthorizationAuthenticator> serviceAuthorizationAuthenticator;

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {
        serviceAuthorizationAuthenticator.getObject().authorizeServiceToken(request);
        return true;
    }
}
