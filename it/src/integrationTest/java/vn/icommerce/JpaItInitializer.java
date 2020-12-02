package vn.icommerce;

import java.util.Optional;
import org.flywaydb.core.Flyway;
import org.slf4j.MDC;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public class JpaItInitializer extends AbstractItInitializer {

  private static JdbcDatabaseContainer jdbcDatabaseContainer;

  static {
    if (PIPELINE_ENV) {
      startDbContainer();
      initDataForDbContainer();

      System.setProperty("spring.jpa.properties.hibernate.generate_statistics", "false");
    }
  }

  private static void startDbContainer() {
    jdbcDatabaseContainer = new PostgreSQLContainer("postgres:11.5")
        .withStartupTimeoutSeconds(600);
    jdbcDatabaseContainer.start();
  }

  private static void initDataForDbContainer() {
    Flyway
        .configure()
        .dataSource(
            jdbcDatabaseContainer.getJdbcUrl(),
            jdbcDatabaseContainer.getUsername(),
            jdbcDatabaseContainer.getPassword())
        .locations(String.format(
            "filesystem:%s/../db/migration",
            System.getProperty("user.dir")))
        .load()
        .migrate();
  }

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    MDC.put("traceId", "testTraceId");
    MDC.put("spanId", "testSpanId");
    Optional
        .ofNullable(jdbcDatabaseContainer)
        .ifPresent(container -> useContainerForDbTest(applicationContext));
  }

  private void useContainerForDbTest(ConfigurableApplicationContext applicationContext) {
    TestPropertyValues
        .of(
            "spring.datasource.url=" + jdbcDatabaseContainer.getJdbcUrl(),
            "spring.datasource.username=" + jdbcDatabaseContainer.getUsername(),
            "spring.datasource.password=" + jdbcDatabaseContainer.getPassword())
        .applyTo(applicationContext.getEnvironment());
  }
}
