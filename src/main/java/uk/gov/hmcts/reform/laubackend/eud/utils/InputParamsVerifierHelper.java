package uk.gov.hmcts.reform.laubackend.eud.utils;

import uk.gov.hmcts.reform.laubackend.eud.dto.UserDataGetRequestParams;
import uk.gov.hmcts.reform.laubackend.eud.exceptions.InvalidRequestException;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static uk.gov.hmcts.reform.laubackend.eud.constants.ExceptionMessageConstants.USERID_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.eud.constants.ExceptionMessageConstants.appendExceptionParameter;

public final class InputParamsVerifierHelper {

    private InputParamsVerifierHelper() {
    }

    public static void verifyUserDataGetRequestParams(final UserDataGetRequestParams params)
        throws InvalidRequestException {
        String userId = params.userId();
        if (!isEmpty(userId) && userId.length() > 64) {
            throw new InvalidRequestException(appendExceptionParameter(
                USERID_GET_EXCEPTION_MESSAGE, userId), BAD_REQUEST);
        }
    }
}
