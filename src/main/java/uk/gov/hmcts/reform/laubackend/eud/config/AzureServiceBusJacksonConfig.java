package uk.gov.hmcts.reform.laubackend.eud.config;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.spring.messaging.converter.AzureMessageConverter;
import com.azure.spring.messaging.servicebus.implementation.support.converter.ServiceBusMessageConverter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureServiceBusJacksonConfig {

    @Bean
    AzureMessageConverter<ServiceBusReceivedMessage, ServiceBusMessage> serviceBusMessageConverter() {
        JsonMapper jsonMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

        return new ServiceBusMessageConverter(jsonMapper);
    }
}
