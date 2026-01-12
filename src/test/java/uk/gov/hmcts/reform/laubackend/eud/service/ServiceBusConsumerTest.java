package uk.gov.hmcts.reform.laubackend.eud.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ExtendWith(OutputCaptureExtension.class)
class ServiceBusConsumerTest {
    private final ServiceBusConsumer consumer = new ServiceBusConsumer();

    @Test
    void onAddMessage_shouldLog(CapturedOutput output) {
        consumer.onAddMessage("hello-add");
        await().atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> assertThat(output.getOut() + output.getErr())
                .contains("Received Add message from ServiceBusListener: hello-add"));
    }

    @Test
    void onModifyMessage_shouldLog(CapturedOutput output) {
        consumer.onModifyMessage("hello-modify");
        await().atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> assertThat(output.getOut() + output.getErr())
                .contains("Received Modify message from ServiceBusListener: hello-modify"));
    }

    @Test
    void onRemoveMessage_shouldLog(CapturedOutput output) {
        consumer.onRemoveMessage("hello-remove");
        await().atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> assertThat(output.getOut() + output.getErr())
                .contains("Received Remove message from ServiceBusListener: hello-remove"));
    }
}
