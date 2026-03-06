package uk.gov.hmcts.reform.laubackend.eud.service;

import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.spring.messaging.servicebus.implementation.core.annotation.ServiceBusListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.laubackend.eud.dto.IdamEvent;
import uk.gov.hmcts.reform.laubackend.eud.exceptions.MessageProcessingException;

import java.util.List;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBooleanProperty(prefix = "lau.servicebus", name = "enabled")
public class ServiceBusConsumer {

    private static final int FIRST_MSG = 1;
    private static final int MAX_BYTES = 1024 * 16; // 16kb, standard message is around 2kb
    private final ServiceBusMessageHandler messageHandler;

    @Qualifier("serviceBusObjectMapper")
    private final ObjectMapper objectMapper;

    private final Validator validator;

    @ServiceBusListener(destination = "${lau.servicebus.topic.add}", group = "${lau.servicebus.subscription.add}")
    public void onAddMessage(ServiceBusReceivedMessage message) {
        handleIncoming(message, messageHandler::handleAddMessage);
    }

    @ServiceBusListener(destination = "${lau.servicebus.topic.modify}", group = "${lau.servicebus.subscription.modify}")
    public void onModifyMessage(ServiceBusReceivedMessage message) {
        handleIncoming(message, messageHandler::handleModifyMessage);
    }

    @ServiceBusListener(destination = "${lau.servicebus.topic.remove}", group = "${lau.servicebus.subscription.remove}")
    public void onRemoveMessage(IdamEvent event) {
        // log an error as we don't expect these events for now
        log.error("Received Remove message from ServiceBusListener on {}", event.eventDateTimeUtc());
    }

    private void handleIncoming(ServiceBusReceivedMessage message, Consumer<IdamEvent> consumer) {
        long deliveryCount = message.getDeliveryCount();
        if (message.getBody() == null || message.getBody().getLength() > MAX_BYTES) {
            String errorMsg = message.getMessageId() + " PRE_VALIDATION failed, body missing or too large";
            if (deliveryCount == FIRST_MSG) {
                log.error(errorMsg);
            }
            throw new MessageProcessingException(errorMsg);
        }

        // 1. Deserialize
        IdamEvent event;
        try {
            event = objectMapper.readValue(message.getBody().toString(), IdamEvent.class);
        } catch (Exception e) {
            String errorMsg = message.getMessageId() + " DESERIALIZATION_FAILED, Failed to parse message body";
            if (deliveryCount == FIRST_MSG) {
                log.error(errorMsg);
            }
            throw new MessageProcessingException(errorMsg);
        }

        // 2. Validate
        List<String> violations = validator.validate(event)
            .stream()
            .map(v -> v.getPropertyPath() + " " + v.getMessage())
            .toList();
        if (!violations.isEmpty()) {
            String errorMsg = message.getMessageId() + " VALIDATION_FAILED, " + String.join("; ", violations);
            if (deliveryCount == FIRST_MSG) {
                log.error(errorMsg);
            }
            throw new MessageProcessingException(errorMsg);
        }

        // 3. Process
        consumer.accept(event);
    }
}
