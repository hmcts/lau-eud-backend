package uk.gov.hmcts.reform.laubackend.eud.controllers;

import net.serenitybdd.annotations.WithTag;
import net.serenitybdd.annotations.WithTags;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
public class EndpointSecurityTest extends LauEudBaseFunctionalTest {

    @Test
    public void shouldAllowUnauthenticatedRequestsToHealthCheck() {

        String response = lauEudBackEndServiceClient.getHealthPage();

        assertThat(response).contains("UP");
    }
}
