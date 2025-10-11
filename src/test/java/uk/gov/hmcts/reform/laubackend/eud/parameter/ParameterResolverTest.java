package uk.gov.hmcts.reform.laubackend.eud.parameter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class ParameterResolverTest {

    private static final String IDAM_API_URL = "idamHost";
    private static final String CLIENT_ID = "clientId";
    private static final String CLIENT_SECRET = "clientSecret";

    private final ParameterResolver resolver = new ParameterResolver();

    @BeforeEach
    public void initMock() {
        ReflectionTestUtils.setField(resolver, IDAM_API_URL, "http://locahost:5000");
        ReflectionTestUtils.setField(resolver, CLIENT_ID, "client id");
        ReflectionTestUtils.setField(resolver, CLIENT_SECRET, "client secret");
    }

    @Test
    void shouldGetIdamHost() {
        assertThat(resolver.getIdamHost()).isEqualTo("http://locahost:5000");
    }

    @Test
    void shouldGetClientId() {
        assertThat(resolver.getClientId()).isEqualTo("client id");
    }

    @Test
    void shouldGetClientSecret() {
        assertThat(resolver.getClientSecret()).isEqualTo("client secret");
    }

}
