package uk.gov.hmcts.reform.laubackend.eud.bdd;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Getter;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Getter
@SuppressWarnings({"PMD.NonSerializableClass"})
public enum WireMockInstantiator {
    INSTANCE;
    private final WireMockServer wireMockServer;

    WireMockInstantiator() {
        wireMockServer = new WireMockServer(
                options()
                        .port(4554)
                        .usingFilesUnderClasspath("wiremock")
        );
        wireMockServer.start();
    }

    public static WireMockServer getWireMockInstance() {
        return INSTANCE.getWireMockServer();
    }
}
