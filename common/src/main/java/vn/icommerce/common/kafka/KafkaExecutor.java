package vn.icommerce.common.kafka;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
    value = "common.infra.kafka.producer.enable",
    havingValue = "true")
public class KafkaExecutor {

  private final KafkaOperations<String, Object> kafkaOperations;

  public KafkaExecutor(KafkaOperations<String, Object> kafkaOperations) {
    this.kafkaOperations = kafkaOperations;
  }

  public void publish(Message event) {
    try {
      kafkaOperations.send(event).get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
