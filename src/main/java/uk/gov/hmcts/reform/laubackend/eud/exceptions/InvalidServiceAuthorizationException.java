package uk.gov.hmcts.reform.laubackend.eud.exceptions;

public class InvalidServiceAuthorizationException extends RuntimeException {
    private static final long serialVersionUID = -5L;

    public InvalidServiceAuthorizationException(final String message) {
        super(message);
    }
}
