package uk.gov.hmcts.reform.laubackend.eud.bdd;

import io.cucumber.java.Before;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class FlywayHooks {

    // Forces DB migrations to run once before any Cucumber scenarios starts.
    private static final AtomicBoolean MIGRATED = new AtomicBoolean(false);

    @Autowired
    private DataSource dataSource;

    @Value("${spring.flyway.placeholders.LAU_EUD_DB_PASSWORD:laupass}")
    private String dbPassword;

    @Before
    public void runFlywayMigrations() {
        if (MIGRATED.compareAndSet(false, true)) {
            Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .placeholders(Map.of("LAU_EUD_DB_PASSWORD", dbPassword))
                .load()
                .migrate();
        }
    }
}
