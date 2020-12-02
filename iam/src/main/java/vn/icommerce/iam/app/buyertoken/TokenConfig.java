package vn.icommerce.iam.app.buyertoken;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("iam.infra.token")
public class TokenConfig {

  /**
   * Max retries for failed log in by pincode
   */
  private int maxAuthRetries = 5;

  /**
   * Token expiration duration in second. Default: 30 minutes.
   */
  private long expirationInS = 30 * 60;
}
