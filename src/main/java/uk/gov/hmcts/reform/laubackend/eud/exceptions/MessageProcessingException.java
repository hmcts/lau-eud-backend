package uk.gov.hmcts.reform.laubackend.eud.exceptions;

public class MessageProcessingException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MessageProcessingException(final String message) {
        super(message);
    }

    public MessageProcessingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
