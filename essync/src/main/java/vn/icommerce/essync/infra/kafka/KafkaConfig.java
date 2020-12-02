package vn.icommerce.essync.infra.kafka;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import vn.icommerce.sharedkernel.domain.event.ProductCreatedEvent;
import vn.icommerce.sharedkernel.domain.event.ProductUpdatedEvent;

/**
 * Configuration for Kafka cluster.
 */
@Data
@Configuration
@ConfigurationProperties("essync.infra.kafka")
@Slf4j
public class KafkaConfig {

  /**
   * The topic id of the product created event.
   */
  private String productCreatedEventTopicId =
      ProductCreatedEvent.class.getSimpleName();

  /**
   * The topic id of the product updated event.
   */
  private String productUpdatedEventTopicId =
      ProductUpdatedEvent.class.getSimpleName();
}
