package uk.gov.hmcts.reform.laubackend.eud.controllers;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.annotations.WithTag;
import net.serenitybdd.annotations.WithTags;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.laubackend.eud.client.LauEudBackEndServiceClient;

@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Functional")})
@ComponentScan("uk.gov.hmcts.reform.laubackend.eud")
@TestPropertySource("classpath:application-functional.yaml")
@Slf4j
@SuppressWarnings({"PMD.AbstractClassWithoutAbstractMethod"})
public abstract class LauEudBaseFunctionalTest {

    @Value("${targetInstance}")
    protected String lauEudBackendApiUrl;

    protected LauEudBackEndServiceClient lauEudBackEndServiceClient;

    @Before
    public void setUp() {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.defaultParser = Parser.JSON;

        lauEudBackEndServiceClient = new LauEudBackEndServiceClient(lauEudBackendApiUrl);
    }
}
