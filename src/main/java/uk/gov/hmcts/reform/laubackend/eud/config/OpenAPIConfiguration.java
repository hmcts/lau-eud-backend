package uk.gov.hmcts.reform.laubackend.eud.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    private static final String ACCOUNT_UPDATES_API_METHOD = "getUserAccountUpdates";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info().title("lau-eud-backend")
                      .description("lau-eud-backend")
                      .version("v0.0.1")
                      .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")))
            .externalDocs(new ExternalDocumentation()
                              .description("README")
                              .url("https://github.com/hmcts/lau-eud-backend"));
    }

    @Bean
    public OperationCustomizer hideSortForGetUserAccountUpdates() {
        return (operation, handlerMethod) -> {
            if (ACCOUNT_UPDATES_API_METHOD.equals(handlerMethod.getMethod().getName())) {
                if (operation.getParameters() != null) {
                    operation.getParameters().removeIf(p -> "sort".equals(p.getName()));
                }
            }
            return operation;
        };
    }

}
