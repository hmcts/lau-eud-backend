package uk.gov.hmcts.reform.laubackend.eud.exceptions;

public class ServiceAuthTokenGenerationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ServiceAuthTokenGenerationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ServiceAuthTokenGenerationException(final String message) {
        super(message);
    }
}
