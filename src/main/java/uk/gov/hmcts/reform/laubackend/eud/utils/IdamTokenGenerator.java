package uk.gov.hmcts.reform.laubackend.eud.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.idam.client.models.TokenResponse;
import uk.gov.hmcts.reform.laubackend.eud.exceptions.IdamAuthTokenGenerationException;
import uk.gov.hmcts.reform.laubackend.eud.parameter.ParameterResolver;
import uk.gov.hmcts.reform.laubackend.eud.service.remote.client.IdamClient;

@Service
@Slf4j
@Getter
@RequiredArgsConstructor
public class IdamTokenGenerator {

    public static final String BEARER = "Bearer ";
    public static final String IDAM_GRANT_TYPE = "client_credentials";
    public static final String IDAM_SCOPE = "view-user";

    private final IdamClient idamClient;
    private final ParameterResolver parameterResolver;

    private String idamClientToken = "token";

    public String generateIdamToken() {
        try {
            TokenResponse tokenResponse = idamClient.getToken(
                parameterResolver.getClientId(),
                parameterResolver.getClientSecret(),
                null,
                IDAM_GRANT_TYPE,
                IDAM_SCOPE
            );
            idamClientToken = tokenResponse.accessToken;

        } catch (final Exception exception) {
            String msg = String.format("Unable to generate IDAM token due to error - %s", exception.getMessage());
            log.error(msg, exception);
            throw new IdamAuthTokenGenerationException(msg, exception);
        }
        return BEARER + idamClientToken;
    }
}
