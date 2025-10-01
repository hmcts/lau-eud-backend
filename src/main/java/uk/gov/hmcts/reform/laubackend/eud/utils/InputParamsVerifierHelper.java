package uk.gov.hmcts.reform.laubackend.eud.utils;

import uk.gov.hmcts.reform.laubackend.eud.exceptions.InvalidRequestException;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static uk.gov.hmcts.reform.laubackend.eud.constants.ExceptionMessageConstants.appendExceptionParameter;

public final class InputParamsVerifierHelper {

    private InputParamsVerifierHelper() {
    }

    public static void verifyUserId(final String userId,
                                    final String exceptionMessage) throws InvalidRequestException {
        if (!isEmpty(userId) && userId.length() > 64) {
            throw new InvalidRequestException(appendExceptionParameter(exceptionMessage, userId), BAD_REQUEST);
        }
    }
}
