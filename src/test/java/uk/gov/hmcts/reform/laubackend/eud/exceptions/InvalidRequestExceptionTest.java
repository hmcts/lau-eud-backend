package uk.gov.hmcts.reform.laubackend.eud.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class InvalidRequestExceptionTest {

    @Test
    void shouldCreateInstanceOfException() {
        final InvalidRequestException invalidRequestException = new InvalidRequestException("error", BAD_REQUEST);
        assertThat(invalidRequestException).isInstanceOf(Exception.class);
    }

    @Test
    void shouldCreateInvalidRequestException() {
        final InvalidRequestException invalidRequestException = new InvalidRequestException("error", BAD_REQUEST);
        assertThat(invalidRequestException.getMessage()).isEqualTo("error");
        assertThat(invalidRequestException.getErrorCode()).isEqualTo(BAD_REQUEST);
    }
}
