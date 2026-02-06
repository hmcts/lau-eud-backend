package uk.gov.hmcts.reform.laubackend.eud.controllers;

import net.serenitybdd.annotations.WithTag;
import net.serenitybdd.annotations.WithTags;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@WithTags({@WithTag("testType:Functional")})
@ActiveProfiles("functional")
public class EndpointSecurityTest extends LauEudBaseFunctionalTest {

    @Test
    public void shouldAllowUnauthenticatedRequestsToHealthCheck() {

        String response = lauEudBackEndServiceClient.getHealthPage();

        assertThat(response).contains("UP");
    }
}
