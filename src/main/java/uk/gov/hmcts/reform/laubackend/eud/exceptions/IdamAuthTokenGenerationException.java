package uk.gov.hmcts.reform.laubackend.eud.exceptions;

public class IdamAuthTokenGenerationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public IdamAuthTokenGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
