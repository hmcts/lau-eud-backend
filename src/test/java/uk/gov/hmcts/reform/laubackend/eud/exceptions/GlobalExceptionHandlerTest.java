package uk.gov.hmcts.reform.laubackend.eud.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @Test
    void shouldReturnBadRequestWithErrorMessage() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "userId", "userId must not be empty");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test/path");

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ProblemDetail pd = handler.handleValidationException(exception, request);

        assertEquals("Bad Request", pd.getTitle());
        assertEquals("One or more fields are invalid.", pd.getDetail());
        assertEquals(400, pd.getStatus());
    }
}

