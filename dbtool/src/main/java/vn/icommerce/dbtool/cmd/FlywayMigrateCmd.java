package vn.icommerce.dbtool.cmd;

import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;

@Component
public class FlywayMigrateCmd extends AbstractFlywayCmd {

  private final Flyway flyway;

  public FlywayMigrateCmd(Flyway flyway) {
    this.flyway = flyway;
  }

  @Override
  public void run() {
    flyway.migrate();
  }
}
