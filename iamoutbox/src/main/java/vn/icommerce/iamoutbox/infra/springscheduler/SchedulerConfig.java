

package vn.icommerce.iamoutbox.infra.springscheduler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration for OutboxMarker Worker.
 *
 */
@Data
@Configuration
@EnableScheduling
@ConfigurationProperties("outbox.infra.springscheduler")
public class SchedulerConfig {

  /**
   * Live time out consumed event Id.
   */
  private long retentionDay = 3;

  /**
   * The time delay in ms.
   */
  private long timeDelayMs = 1000;

  /**
   * The cron job syntax to clear trash.
   */
  private String clearTrashCron = "0 0 0 * * *";
}
