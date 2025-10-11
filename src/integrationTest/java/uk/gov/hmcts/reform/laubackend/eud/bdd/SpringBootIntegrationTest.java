package uk.gov.hmcts.reform.laubackend.eud.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.reform.laubackend.eud.Application;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@CucumberContextConfiguration
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@Slf4j
@ActiveProfiles("test")
@SuppressWarnings("PMD.TestClassWithoutTestCases")
public class SpringBootIntegrationTest {
}
