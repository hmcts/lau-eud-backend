package uk.gov.hmcts.reform.laubackend.eud.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceBusConsumerConditionalTest {
    private final ApplicationContextRunner contextRunner =
        new ApplicationContextRunner().withUserConfiguration(ServiceBusConsumer.class)
            .withBean(ServiceBusMessageHandler.class, () -> null);

    @Test
    void shouldNotCreateConsumerBeanByDefault() {
        contextRunner.run(context ->
                              assertThat(context).doesNotHaveBean(ServiceBusConsumer.class)
        );
    }

    @Test
    void shouldCreateConsumerBeanWhenEnabledTrue() {
        contextRunner
            .withPropertyValues("lau.servicebus.enabled=true")
            .run(context ->
                     assertThat(context).hasSingleBean(ServiceBusConsumer.class)
            );
    }

    @Test
    void shouldNotCreateConsumerBeanWhenEnabledFalse() {
        contextRunner
            .withPropertyValues("lau.servicebus.enabled=false")
            .run(context ->
                     assertThat(context).doesNotHaveBean(ServiceBusConsumer.class)
            );
    }
}
