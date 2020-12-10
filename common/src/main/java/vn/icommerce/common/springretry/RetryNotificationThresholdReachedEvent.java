

package vn.icommerce.common.springretry;

import lombok.Data;
import lombok.experimental.Accessors;


@Accessors(chain = true)
@Data
public class RetryNotificationThresholdReachedEvent {

  private int retryCount;

  private int retryNotificationThreshold;

  private String stackTrace;
}
