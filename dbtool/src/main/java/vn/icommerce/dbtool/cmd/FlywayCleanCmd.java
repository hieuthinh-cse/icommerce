package vn.icommerce.dbtool.cmd;

import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;

@Component
public class FlywayCleanCmd extends AbstractFlywayCmd {

  private final Flyway flyway;

  public FlywayCleanCmd(Flyway flyway) {
    this.flyway = flyway;
  }

  @Override
  public void run() {
    flyway.clean();
  }
}
