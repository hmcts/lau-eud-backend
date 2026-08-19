package uk.gov.hmcts.reform.laubackend.eud.config;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.spring.messaging.converter.AzureMessageConverter;
import com.azure.spring.messaging.servicebus.implementation.support.converter.ServiceBusMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class AzureServiceBusJacksonConfig {

    @Bean
    AzureMessageConverter<ServiceBusReceivedMessage, ServiceBusMessage> serviceBusMessageConverter() {
        JsonMapper jsonMapper = JsonMapper.builder().build();

        return new ServiceBusMessageConverter(jsonMapper);
    }
}
