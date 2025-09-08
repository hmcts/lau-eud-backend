package uk.gov.hmcts.reform.laubackend.eud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
public class Application {

    public static void main(final String[] args) {
        System.getenv().forEach((key, value) -> System.out.println(key + ": " + value));
        SpringApplication.run(Application.class, args);
    }
}
