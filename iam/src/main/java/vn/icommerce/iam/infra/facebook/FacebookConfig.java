package vn.icommerce.iam.infra.facebook;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Bean configuration for Argon2 hashing function.
 */
@Configuration
@Data
@ConfigurationProperties("iam.infra.facebook")
public class FacebookConfig {

  private Long appId = 125857037951010L;

  private String appSecret = "d858f61ed146c469159dce7ad834e3c2";
}
