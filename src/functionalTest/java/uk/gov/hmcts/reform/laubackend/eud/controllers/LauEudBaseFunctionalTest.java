package uk.gov.hmcts.reform.laubackend.eud.controllers;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.annotations.WithTag;
import net.serenitybdd.annotations.WithTags;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.laubackend.eud.client.LauEudBackEndServiceClient;

@ExtendWith({SerenityJUnit5Extension.class, SpringExtension.class})
@WithTags({@WithTag("testType:Functional")})
@ComponentScan("uk.gov.hmcts.reform.laubackend.eud")
@TestPropertySource("classpath:application-functional.yaml")
@Slf4j
@SuppressWarnings({"PMD.AbstractClassWithoutAbstractMethod"})
public abstract class LauEudBaseFunctionalTest {

    @Value("${targetInstance}")
    protected String lauEudBackendApiUrl;

    protected LauEudBackEndServiceClient lauEudBackEndServiceClient;

    @BeforeEach
    public void setUp() {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.defaultParser = Parser.JSON;

        lauEudBackEndServiceClient = new LauEudBackEndServiceClient(lauEudBackendApiUrl);
    }
}
