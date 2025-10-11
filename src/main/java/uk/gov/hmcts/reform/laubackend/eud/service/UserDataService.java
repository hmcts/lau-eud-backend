package uk.gov.hmcts.reform.laubackend.eud.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.eud.dto.UserDataGetRequestParams;
import uk.gov.hmcts.reform.laubackend.eud.response.UserDataResponse;
import uk.gov.hmcts.reform.laubackend.eud.service.remote.client.IdamClient;
import uk.gov.hmcts.reform.laubackend.eud.utils.IdamTokenGenerator;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDataService {

    private final IdamClient client;
    private final IdamTokenGenerator idamTokenGenerator;

    public UserDataResponse getUserData(final UserDataGetRequestParams params) {
        boolean hasUserId = !isEmpty(params.getUserId());
        String token = idamTokenGenerator.generateIdamToken();
        if (hasUserId) {
            return client.getUserDataByUserId(token, params.getUserId().trim());
        }
        return client.getUserDataByEmail(token, params.getEmail().trim());
    }
}
