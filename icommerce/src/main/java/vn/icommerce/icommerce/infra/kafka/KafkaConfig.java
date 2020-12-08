package vn.icommerce.icommerce.infra.kafka;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import vn.icommerce.sharedkernel.domain.event.BuyerCreatedEvent;
import vn.icommerce.sharedkernel.domain.event.OrderCreatedEvent;

/**
 * Configuration for Kafka cluster.
 */
@Data
@Configuration
@ConfigurationProperties("icommerce.infra.kafka")
@Slf4j
public class KafkaConfig {

  /**
   * The topic id of the buyer account created event.
   */
  private String buyerCreatedEventTopicId =
      BuyerCreatedEvent.class.getSimpleName();

  /**
   * The topic id of the order created event.
   */
  private String orderCreatedEventTopicId =
      OrderCreatedEvent.class.getSimpleName();
}
