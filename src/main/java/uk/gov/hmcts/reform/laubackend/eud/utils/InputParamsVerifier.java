package uk.gov.hmcts.reform.laubackend.eud.utils;

import uk.gov.hmcts.reform.laubackend.eud.dto.UserDataGetRequestParams;
import uk.gov.hmcts.reform.laubackend.eud.exceptions.InvalidRequestException;

import static uk.gov.hmcts.reform.laubackend.eud.constants.ExceptionMessageConstants.USERID_GET_EXCEPTION_MESSAGE;
import static uk.gov.hmcts.reform.laubackend.eud.utils.InputParamsVerifierHelper.verifyUserId;

public final class InputParamsVerifier {

    public static void verifyUserDataGetRequestParams(
        final UserDataGetRequestParams params
    ) throws InvalidRequestException {
        verifyUserId(params.userId(), USERID_GET_EXCEPTION_MESSAGE);
    }
}
