package uk.gov.hmcts.reform.laubackend.eud.service;

import com.azure.core.util.BinaryData;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import uk.gov.hmcts.reform.laubackend.eud.domain.EventType;
import uk.gov.hmcts.reform.laubackend.eud.dto.IdamEvent;
import uk.gov.hmcts.reform.laubackend.eud.exceptions.MessageProcessingException;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({OutputCaptureExtension.class, MockitoExtension.class})
class ServiceBusConsumerTest {
    @Mock
    ServiceBusMessageHandler serviceBusMessageHandler;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    Validator validator;

    @InjectMocks
    private ServiceBusConsumer consumer;

    @ParameterizedTest
    @ValueSource(strings = {"ADD", "MODIFY"})
    void onMessage_whenMessageValid_shouldCallModifyHandler(String eventType) throws JsonProcessingException {
        ServiceBusReceivedMessage message = mock(ServiceBusReceivedMessage.class);
        BinaryData body = mock(BinaryData.class);

        when(message.getBody()).thenReturn(body);
        when(body.getLength()).thenReturn(100L);
        when(body.toString()).thenReturn("{\"eventType\":\"" + eventType + "\"}");

        LocalDateTime ts = LocalDateTime.parse("2026-03-01T12:23:43");
        IdamEvent event = new IdamEvent(EventType.MODIFY, null, null, null, null, ts);
        when(objectMapper.readValue(anyString(), eq(IdamEvent.class))).thenReturn(event);
        when(validator.validate(event)).thenReturn(Set.of());

        consumer.onModifyMessage(message);

        verify(objectMapper, times(1)).readValue(anyString(), eq(IdamEvent.class));
        verify(validator, times(1)).validate(event);
        verify(serviceBusMessageHandler, times(1)).handleModifyMessage(event);
    }

    @Test
    void onModifyMessage_whenBodyNull_shouldThrowException() {
        ServiceBusReceivedMessage message = mock(ServiceBusReceivedMessage.class);
        when(message.getBody()).thenReturn(null);
        when(message.getMessageId()).thenReturn("mid-2");
        when(message.getDeliveryCount()).thenReturn(1L);

        assertThatThrownBy(() -> consumer.onModifyMessage(message))
            .isInstanceOf(MessageProcessingException.class)
            .hasMessageContaining("mid-2")
            .hasMessageContaining("PRE_VALIDATION failed");

        verify(serviceBusMessageHandler, never()).handleModifyMessage(any());
    }

    @Test
    void onModifyMessage_whenBodyTooLarge_shouldThrowException() {
        ServiceBusReceivedMessage message = mock(ServiceBusReceivedMessage.class);
        BinaryData body = mock(BinaryData.class);

        when(message.getBody()).thenReturn(body);
        when(body.getLength()).thenReturn(16L * 1024 + 1); // > 16kb
        when(message.getMessageId()).thenReturn("mid-3");
        when(message.getDeliveryCount()).thenReturn(1L);

        assertThatThrownBy(() -> consumer.onModifyMessage(message))
            .isInstanceOf(MessageProcessingException.class)
            .hasMessageContaining("mid-3")
            .hasMessageContaining("PRE_VALIDATION failed");

        verify(serviceBusMessageHandler, never()).handleModifyMessage(any());
    }


    @Test
    void onModifyMessage_whenDeserializationFails_shouldThrowException() throws Exception {
        ServiceBusReceivedMessage message = mock(ServiceBusReceivedMessage.class);
        BinaryData body = mock(BinaryData.class);

        when(message.getBody()).thenReturn(body);
        when(body.getLength()).thenReturn(100L);
        when(body.toString()).thenReturn("not-json");
        when(message.getMessageId()).thenReturn("mid-4");
        when(message.getDeliveryCount()).thenReturn(1L);

        when(objectMapper.readValue(anyString(), eq(IdamEvent.class)))
            .thenThrow(new RuntimeException(new IOException("boom")));

        assertThatThrownBy(() -> consumer.onModifyMessage(message))
            .isInstanceOf(MessageProcessingException.class)
            .hasMessageContaining("mid-4")
            .hasMessageContaining("DESERIALIZATION_FAILED");

        verify(serviceBusMessageHandler, never()).handleModifyMessage(any());
    }

    @Test
    void onModifyMessage_whenValidationFails_shouldThrowException() throws Exception {
        ServiceBusReceivedMessage message = mock(ServiceBusReceivedMessage.class);
        BinaryData body = mock(BinaryData.class);

        when(message.getBody()).thenReturn(body);
        when(body.getLength()).thenReturn(100L);
        when(body.toString()).thenReturn("{\"eventType\":\"MODIFY\"}");
        when(message.getMessageId()).thenReturn("mid-5");
        when(message.getDeliveryCount()).thenReturn(1L);

        LocalDateTime ts = LocalDateTime.parse("2026-03-01T12:23:43");
        IdamEvent event = new IdamEvent(EventType.MODIFY, null, null, null, null, ts);
        when(objectMapper.readValue(anyString(), eq(IdamEvent.class))).thenReturn(event);

        @SuppressWarnings("unchecked")
        ConstraintViolation<IdamEvent> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("eventType");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be null");

        when(validator.validate(event)).thenReturn(Set.of(violation));

        assertThatThrownBy(() -> consumer.onModifyMessage(message))
            .isInstanceOf(MessageProcessingException.class)
            .hasMessageContaining("mid-5")
            .hasMessageContaining("VALIDATION_FAILED")
            .hasMessageContaining("eventType")
            .hasMessageContaining("must not be null");

        verify(serviceBusMessageHandler, never()).handleModifyMessage(any());
    }

    @Test
    void onRemoveMessage_shouldLog(CapturedOutput output) {
        LocalDateTime timestamp = LocalDateTime.parse("2026-03-01T12:23:43");
        IdamEvent event = new IdamEvent(EventType.REMOVE, null, null, null, null, timestamp);
        consumer.onRemoveMessage(event);
        await().atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> assertThat(output.getOut() + output.getErr())
                .contains("Received Remove message from ServiceBusListener on 2026-03-01T12:23:43Z"));
    }
}
