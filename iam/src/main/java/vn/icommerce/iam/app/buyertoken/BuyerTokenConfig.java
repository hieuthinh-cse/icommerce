package vn.icommerce.iam.app.buyertoken;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("alliance.app.token")
public class BuyerTokenConfig {

  /**
   * Token expiration duration in second. Default: 2 hours.
   */
  private long expirationInS = 60 * 60 * 2;
}
