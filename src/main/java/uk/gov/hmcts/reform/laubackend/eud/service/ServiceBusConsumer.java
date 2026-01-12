package uk.gov.hmcts.reform.laubackend.eud.service;

import com.azure.spring.messaging.servicebus.implementation.core.annotation.ServiceBusListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnBooleanProperty(prefix = "lau.servicebus", name = "enabled")
public class ServiceBusConsumer {
    @ServiceBusListener(destination = "${lau.servicebus.topic.add}", group = "${lau.servicebus.subscription.add}")
    public void onAddMessage(String message) {
        log.info("Received Add message from ServiceBusListener: {}", message);
    }

    @ServiceBusListener(destination = "${lau.servicebus.topic.modify}", group = "${lau.servicebus.subscription.modify}")
    public void onModifyMessage(String message) {
        log.info("Received Modify message from ServiceBusListener: {}", message);
    }

    @ServiceBusListener(destination = "${lau.servicebus.topic.remove}", group = "${lau.servicebus.subscription.remove}")
    public void onRemoveMessage(String message) {
        log.info("Received Remove message from ServiceBusListener: {}", message);
    }
}
