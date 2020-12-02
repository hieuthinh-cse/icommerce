package vn.icommerce.dbtool.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.icommerce.dbtool.DbTool;

/**
 * Configures the flyway library.
 *
 */
@Configuration
public class FlywayConfig {

  /**
   * Returns the strategy that does nothing to delegate the logic to the {@link
   * DbTool#run(ApplicationArguments)} method.
   *
   * @return the noop strategy
   */
  @Bean
  public FlywayMigrationStrategy flywayMigrationStrategy() {
    return flyway -> {
    };
  }
}
