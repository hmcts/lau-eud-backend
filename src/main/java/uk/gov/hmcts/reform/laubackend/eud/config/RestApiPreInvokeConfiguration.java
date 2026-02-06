package uk.gov.hmcts.reform.laubackend.eud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.hmcts.reform.laubackend.eud.authorization.RestApiPreInvokeInterceptor;

@Configuration
public class RestApiPreInvokeConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(restApiPreInvokeInterceptor())
            .addPathPatterns("/audit/userData/**", "/audit/userUpdates/**");
    }

    @Bean
    public RestApiPreInvokeInterceptor restApiPreInvokeInterceptor() {
        return new RestApiPreInvokeInterceptor();
    }

}
