package uk.gov.hmcts.reform.laubackend.eud.client;

import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.springframework.http.HttpStatus;

@Slf4j
public class LauEudBackEndServiceClient {
    private final String lauEudBackEndApiUrl;

    public LauEudBackEndServiceClient(String lauEudBackEndApiUrl) {
        this.lauEudBackEndApiUrl = lauEudBackEndApiUrl;
    }

    public String getHealthPage() {
        return SerenityRest
            .get(lauEudBackEndApiUrl + "/health")
            .then()
            .statusCode(HttpStatus.OK.value())
            .and()
            .extract()
            .body()
            .asString();
    }
}
