package uk.gov.hmcts.reform.laubackend.eud.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.eud.dto.UserDataGetRequestParams;
import uk.gov.hmcts.reform.laubackend.eud.parameter.ParameterResolver;
import uk.gov.hmcts.reform.laubackend.eud.response.UserDataResponse;
import uk.gov.hmcts.reform.laubackend.eud.service.remote.client.IdamClient;
import uk.gov.hmcts.reform.laubackend.eud.utils.IdamTokenGenerator;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDataService {

    private final IdamClient client;
    private final IdamTokenGenerator idamTokenGenerator;
    private final ParameterResolver parameterResolver;

    public UserDataResponse getUserData(final UserDataGetRequestParams params) {
        UserDataResponse userDataResponse = null;
        try {
            if (params.userId() != null) {
                userDataResponse = client.getUserDataByUserId(
                    idamTokenGenerator.generateIdamToken(), params.userId()
                );
            } else if (params.email() != null) {
                userDataResponse = client.getUserDataByEmail(
                    idamTokenGenerator.generateIdamToken(), params.email()
                );
            }
        } catch (Exception e) {
            log.error("UserDataService.getUserData threw exception: {}", e.getMessage(), e);
            throw e;
        }
        return userDataResponse;
    }
}
