

package vn.icommerce.common.kafka;

import static vn.icommerce.common.brave.BraveExecutor.CORRELATION_ID;
import static vn.icommerce.common.brave.BraveExecutor.SPAN_ID;
import static vn.icommerce.common.brave.BraveExecutor.TRACE_ID;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer2;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.ExponentialBackOff;
import org.springframework.util.backoff.FixedBackOff;
import vn.icommerce.common.exception.DuplicateException;
import vn.icommerce.common.brave.BraveExecutor;

/**
 * Configuration for Kafka cluster.
 *
 *
 *
 *
 */
@Data
@EnableKafka
@Configuration
@ConditionalOnProperty(
    value = "common.infra.kafka.consumer.enable",
    havingValue = "true")
@ConfigurationProperties("common.infra.kafka.consumer")
@Slf4j
public class KafkaConsumerConfig {

  /**
   * See {@link ConsumerConfig#BOOTSTRAP_SERVERS_CONFIG}.
   */
  private String host = "localhost:9092";

  /**
   * The id of the Hermes consumer group.
   *
   *
   */
  private String groupId = "commonGroup";

  /**
   * See {@link ConsumerConfig#ENABLE_AUTO_COMMIT_CONFIG}.
   */
  private boolean enableAutoCommit = false;

  /**
   * See {@link ConsumerConfig#AUTO_OFFSET_RESET_CONFIG}.
   */
  private String autoOffsetReset = "earliest";

  /**
   * See {@link ProducerConfig#RECONNECT_BACKOFF_MS_CONFIG}.
   */
  private int producerReconnectBackOffMs = 2_000;

  /**
   * See {@link ConsumerConfig#RECONNECT_BACKOFF_MS_CONFIG}.
   */
  private int consumerReconnectBackOffMs = 2_000;

  /**
   * See {@link ConsumerConfig#MAX_POLL_RECORDS_CONFIG}.
   */
  private int maxPollRecords = 100;

  /**
   * See {@link ConsumerConfig#MAX_POLL_INTERVAL_MS_CONFIG}.
   */
  private int maxPollIntervalMs = 600_000;

  /**
   * See {@link ProducerConfig#ACKS_CONFIG}.
   */
  private String acks = "all";

  /**
   * See {@link ProducerConfig#RETRIES_CONFIG}.
   */
  private int retries = 10;

  /**
   * The maximum number of retry attempts for consumer.
   */
  private long consumerRetryMaxAttempts = 10;

  /**
   * The interval between two retry attempts for consumer.
   */
  private int consumerRetryIntervalMs = 2_000;

  /**
   * The topic id of dead-letter event.
   */
  private String deadLetterTopicId = "deadLetterTopic";

  private final ObjectMapper objectMapper;

  private final ProducerCallback producerCallback;

  private final BraveExecutor braveExecutor;

  public KafkaConsumerConfig(
      ObjectMapper objectMapper,
      ProducerCallback producerCallback,
      BraveExecutor braveExecutor) {
    this.producerCallback = producerCallback;
    this.objectMapper = objectMapper;
    this.braveExecutor = braveExecutor;
  }

  private Map<String, Object> producerConfig() {
    return Map.of(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host,
        ProducerConfig.ACKS_CONFIG, acks,
        ProducerConfig.RETRIES_CONFIG, retries,
        ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, producerReconnectBackOffMs);
  }

  private StringSerializer producerKeySerializer() {
    return new StringSerializer();
  }

  private JsonSerializer<Object> producerValueSerializer() {
    return new JsonSerializer<>(objectMapper);
  }

  /**
   * Configures the factory for producer to serialize JSON event.
   *
   * @return the JSON producer factory
   */
  @Bean
  public DefaultKafkaProducerFactory<String, Object> dltProducerFactory() {
    return new DefaultKafkaProducerFactory<>(
        producerConfig(),
        producerKeySerializer(),
        producerValueSerializer());
  }

  /**
   * Configures the Kafka template to send event.
   *
   * @return the Kafka template
   */
  @Bean
  public KafkaTemplate<String, Object> dltTemplate() {
    var template = new KafkaTemplate<>(dltProducerFactory());
    template.setProducerListener(producerCallback);
    return template;
  }

  private Map<String, Object> consumerConfig() {
    return Map.of(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host,
        ConsumerConfig.GROUP_ID_CONFIG, groupId,
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit,
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset,
        ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, consumerReconnectBackOffMs,
        ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords,
        ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
  }

  private ErrorHandlingDeserializer2<String> consumerKeyDeserializer() {
    return new ErrorHandlingDeserializer2<>(new StringDeserializer());
  }

  private ErrorHandlingDeserializer2<Object> consumerValueDeserializer() {
    var jsonDeserializer = new JsonDeserializer<>(objectMapper);
    jsonDeserializer.addTrustedPackages("*");
    jsonDeserializer.configure(
        Collections.singletonMap(JsonDeserializer.VALUE_DEFAULT_TYPE, String.class.getName()),
        false);

    return new ErrorHandlingDeserializer2<>(jsonDeserializer);
  }

  private DefaultKafkaConsumerFactory<String, Object> jsonConsumerFactory() {
    return new DefaultKafkaConsumerFactory<>(
        consumerConfig(),
        consumerKeyDeserializer(),
        consumerValueDeserializer());
  }

  private DeadLetterPublishingRecoverer deadLetterPublishingRecoverer() {
    return new DeadLetterPublishingRecoverer(
        dltTemplate(),
        (r, e) -> {
          var traceId = Optional
              .ofNullable(r.headers().lastHeader(TRACE_ID))
              .map(header -> new String(header.value()))
              .orElse("");

          var spanId = Optional
              .ofNullable(r.headers().lastHeader(SPAN_ID))
              .map(header -> new String(header.value()))
              .orElse("");

          var correlationId = Optional
              .ofNullable(r.headers().lastHeader(CORRELATION_ID))
              .map(header -> new String(header.value()))
              .orElse("");

          braveExecutor.run(
              traceId,
              spanId,
              correlationId,
              () -> log
                  .info("method: deadLetterPublishingRecoverer, value: {} , topic: {} , key: {} , ",
                      r.value(),
                      r.topic(),
                      r.key(),
                      e));

          return new TopicPartition(deadLetterTopicId, r.partition());
        });
  }

  private FixedBackOff fixedBackOff() {
    return new FixedBackOff(consumerRetryIntervalMs, consumerRetryMaxAttempts);
  }

  private SeekToCurrentErrorHandler seekToCurrentErrorHandler() {
    var seekToCurrentErrorHandler = new SeekToCurrentErrorHandler(
        deadLetterPublishingRecoverer(),
        fixedBackOff());
    seekToCurrentErrorHandler.addNotRetryableException(DuplicateException.class);
    seekToCurrentErrorHandler.setCommitRecovered(true);
    return seekToCurrentErrorHandler;
  }

  private StringJsonMessageConverter stringJsonMessageConverter() {
    return new StringJsonMessageConverter(objectMapper);
  }

  /**
   * Configures the Kafka listener container factory.
   *
   * @return the Kafka consumer.
   */
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
    factory.setConsumerFactory(jsonConsumerFactory());
    factory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
    factory.setMessageConverter(stringJsonMessageConverter());
    factory.setMissingTopicsFatal(false);
    factory.setErrorHandler(seekToCurrentErrorHandler());

    // For some unknown reason, if we don't set this property (although it is already default
    // according to the documentation) then a NullPointerException would be raised when attempting
    // to commit
    factory.getContainerProperties().setSyncCommitTimeout(Duration.ofSeconds(60));

    return factory;
  }

  private SeekToCurrentErrorHandler nonRecoverSeekToCurrentErrorHandler() {
    return new SeekToCurrentErrorHandler(new ExponentialBackOff());
  }

  /**
   * Configures the Kafka listener container factory with no recover.
   *
   * @return the Kafka consumer
   */
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Object> nonRecoverKafkaListenerContainerFactory() {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
    factory.setConsumerFactory(jsonConsumerFactory());
    factory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
    factory.setMessageConverter(stringJsonMessageConverter());
    factory.setMissingTopicsFatal(false);
    factory.setErrorHandler(nonRecoverSeekToCurrentErrorHandler());

    // For some unknown reasons, if we don't set this property (although it is already default
    // according to the documentation) then a NullPointerException would be raised when attempting
    // to commit.
    factory.getContainerProperties().setSyncCommitTimeout(Duration.ofSeconds(60));

    return factory;
  }
}
