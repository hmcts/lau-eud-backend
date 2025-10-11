package uk.gov.hmcts.reform.laubackend.eud.constants;

import lombok.NoArgsConstructor;

import static java.lang.String.valueOf;

@NoArgsConstructor
public final class ExceptionMessageConstants {

    public static final String USERID_GET_EXCEPTION_MESSAGE = "Unable to verify userId path parameter pattern: ";

    public static String appendExceptionParameter(final String exceptionMessage,
                                                  final String exceptionParameter) {
        return exceptionMessage.concat(valueOf(exceptionParameter));
    }
}
