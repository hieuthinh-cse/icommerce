

package vn.icommerce.iamoutbox;

import java.util.Objects;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.elasticsearch.ElasticSearchRestHealthContributorAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;

/**
 * This is the entry point of the Outbox app.
 *
 *
 */
@SpringBootApplication(exclude = {
    ElasticsearchAutoConfiguration.class,
    ElasticSearchRestHealthContributorAutoConfiguration.class
})
public class OutboxApp {

  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    System.setProperty(
        "log4j2.contextSelector",
        "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
    System.setProperty(
        "hibernate.types.print.banner",
        "false");
    if (Objects.isNull(System.getProperty("spring.profiles.active"))) {
      System.setProperty("spring.profiles.active", "local");
    }

    SpringApplication.run(OutboxApp.class, args);
  }
}
