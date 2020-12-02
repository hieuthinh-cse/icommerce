package vn.icommerce;

import java.util.Optional;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;

public class KafkaInitializer extends AbstractItInitializer {

  private static KafkaContainer kafkaContainer;

  static {
    if (PIPELINE_ENV) {
      startKafkaContainer();
    }
  }

  private static void startKafkaContainer() {
    kafkaContainer = new KafkaContainer();
    kafkaContainer.addEnv("KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR", "1");
    kafkaContainer.addEnv("KAFKA_TRANSACTION_STATE_LOG_MIN_ISR", "1");
    kafkaContainer.start();
  }

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    TestPropertyValues
        .of("common.infra.kafka.consumer.enable=true",
            "common.infra.kafka.producer.enable=true")
        .applyTo(applicationContext.getEnvironment());
    Optional
        .ofNullable(kafkaContainer)
        .ifPresent(container -> useContainerForKafkaTest(applicationContext));
  }

  private void useContainerForKafkaTest(ConfigurableApplicationContext applicationContext) {
    TestPropertyValues
        .of(
            "common.infra.kafka.consumer.host=" + kafkaContainer.getBootstrapServers(),
            "common.infra.kafka.producer.host=" + kafkaContainer.getBootstrapServers())
        .applyTo(applicationContext.getEnvironment());
  }

}
