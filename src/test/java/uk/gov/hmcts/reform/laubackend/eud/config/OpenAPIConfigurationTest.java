package uk.gov.hmcts.reform.laubackend.eud.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;

import static org.assertj.core.api.Assertions.assertThat;

class OpenAPIConfigurationTest {

    OperationCustomizer customizer;
    Operation operation;

    // Dummy controller just to supply HandlerMethod instances
    static class DummyController {
        public void getUserAccountUpdates() {
        }

        public void someOtherEndpoint() {
        }
    }


    @BeforeEach
    void setUp() {
        OpenAPIConfiguration config = new OpenAPIConfiguration();
        customizer = config.hideSortForGetUserAccountUpdates();

        operation = new Operation()
            .addParametersItem(new Parameter().name("page"))
            .addParametersItem(new Parameter().name("size"))
            .addParametersItem(new Parameter().name("sort"));
    }

    @Test
    void hideSortForGetUserAccountUpdates_removesSortParameter() throws NoSuchMethodException {
        // given
        HandlerMethod handlerMethod = new HandlerMethod(
            new DummyController(),
            DummyController.class.getMethod("getUserAccountUpdates")
        );

        // when
        Operation customized = customizer.customize(operation, handlerMethod);

        // then
        assertThat(customized.getParameters())
            .extracting(Parameter::getName)
            .containsExactlyInAnyOrder("page", "size");
    }

    @Test
    void hideSortForGetUserAccountUpdates_doesNothingForOtherMethods() throws NoSuchMethodException {
        // given
        HandlerMethod handlerMethod = new HandlerMethod(
            new DummyController(),
            DummyController.class.getMethod("someOtherEndpoint")
        );

        // when
        Operation customized = customizer.customize(operation, handlerMethod);

        // then
        assertThat(customized.getParameters())
            .extracting(Parameter::getName)
            .containsExactlyInAnyOrder("page", "size", "sort");
    }
}
