package uk.gov.hmcts.reform.laubackend.eud.utils;

import uk.gov.hmcts.reform.laubackend.eud.dto.UserDataGetRequestParams;
import uk.gov.hmcts.reform.laubackend.eud.exceptions.InvalidRequestException;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static uk.gov.hmcts.reform.laubackend.eud.constants.ExceptionMessageConstants.MISSING_GET_USER_DATA_PARAMETERS_MESSAGE;

public final class NotEmptyInputParamsVerifier {

    public static void verifyUserDataGetRequestParamsPresence(
        final UserDataGetRequestParams params
    ) throws InvalidRequestException {
        if (params == null) {
            throw new InvalidRequestException("Some parameters are required", BAD_REQUEST);
        }
        boolean noUserId = isEmpty(params.userId());
        boolean noEmail = isEmpty(params.email());

        if (noUserId && noEmail) {
            throw new InvalidRequestException(MISSING_GET_USER_DATA_PARAMETERS_MESSAGE, BAD_REQUEST);
        }

    }
}
