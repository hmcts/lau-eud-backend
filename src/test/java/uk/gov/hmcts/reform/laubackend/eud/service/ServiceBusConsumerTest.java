package uk.gov.hmcts.reform.laubackend.eud.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith({OutputCaptureExtension.class, MockitoExtension.class})
class ServiceBusConsumerTest {
    @Mock
    ServiceBusMessageHandler  serviceBusMessageHandler;

    @InjectMocks
    private ServiceBusConsumer consumer;

    @Test
    void onAddMessage_shouldCallAddHandler() {
        consumer.onAddMessage(null);
        verify(serviceBusMessageHandler, times(1)).handleAddMessage(null);
    }

    @Test
    void onModifyMessage_shouldCallModifyHandler() {
        consumer.onModifyMessage(null);
        verify(serviceBusMessageHandler, times(1)).handleMessage(null);
    }

    @Test
    void onRemoveMessage_shouldLog(CapturedOutput output) {
        consumer.onRemoveMessage("hello-remove");
        await().atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> assertThat(output.getOut() + output.getErr())
                .contains("Received Remove message from ServiceBusListener: hello-remove"));
    }
}
