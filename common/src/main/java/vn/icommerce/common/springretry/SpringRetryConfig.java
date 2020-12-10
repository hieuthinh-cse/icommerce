

package vn.icommerce.common.springretry;


import java.util.Collections;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import vn.icommerce.common.exception.DuplicateException;


@Configuration
@Data
@ConfigurationProperties("common.infra.springretry")
public class SpringRetryConfig {

  /**
   * The initial interval in milliseconds for exponential backoff.
   */
  private long exponentialBackOffInitialInterval = 1000;

  /**
   * The maximum back off period in milliseconds for exponential backoff.
   */
  private long exponentialBackOffMaxInterval = 60000;

  /**
   * The multiplier for exponential backoff.
   */
  private double exponentialBackOffMultiplier = 1.1;

  /**
   * The maximum number of attempts for retry.
   */
  private int maxAttempt = Integer.MAX_VALUE;

  /**
   * The threshold to notify the stakeholders.
   */
  private int notificationThreshold = 10;

  @Bean
  public RetryTemplate retryTemplate(RetryListener retryListener) {
    var exponentialBackOffPolicy = new ExponentialBackOffPolicy();
    exponentialBackOffPolicy.setInitialInterval(exponentialBackOffInitialInterval);
    exponentialBackOffPolicy.setMaxInterval(exponentialBackOffMaxInterval);
    exponentialBackOffPolicy.setMultiplier(exponentialBackOffMultiplier);

    var simpleRetryPolicy = new SimpleRetryPolicy(
        maxAttempt,
        Collections.singletonMap(DuplicateException.class, false),
        false,
        true);

    var retryTemplate = new RetryTemplate();
    retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);
    retryTemplate.setRetryPolicy(simpleRetryPolicy);

    retryTemplate.registerListener(retryListener);

    return retryTemplate;
  }
}
