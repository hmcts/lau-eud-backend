package uk.gov.hmcts.reform.laubackend.eud;

import com.azure.spring.messaging.implementation.annotation.EnableAzureMessaging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"uk.gov.hmcts.reform.laubackend.eud"})
@SuppressWarnings("HideUtilityClassConstructor")
@EnableAzureMessaging
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
