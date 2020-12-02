/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.ChainedKafkaTransactionManager;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;

/**
 * Configuration for Kafka cluster.
 *
 * <p>Created on 9/3/19.
 *
 * @author khoanguyenminh
 */
@Data
@EnableKafka
@Configuration
@ConditionalOnProperty(
    value = "common.infra.kafka.producer.enable",
    havingValue = "true")
@ConfigurationProperties("common.infra.kafka.producer")
@Slf4j
public class KafkaProducerConfig {

  /**
   * The Kafka cluster host for client to connect.
   */
  private String host = "localhost:9092";

  /**
   * See {@link ProducerConfig#ACKS_CONFIG}.
   */
  private String acks = "all";

  /**
   * See {@link ProducerConfig#RECONNECT_BACKOFF_MS_CONFIG}.
   */
  private int producerReconnectBackOffMs = 2_000;

  private final ObjectMapper objectMapper;

  private final ProducerCallback producerCallback;

  public KafkaProducerConfig(
      ObjectMapper objectMapper,
      ProducerCallback producerCallback) {
    this.objectMapper = objectMapper;
    this.producerCallback = producerCallback;
  }

  private Map<String, Object> producerConfig() {
    return Map.of(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host,
        ProducerConfig.ACKS_CONFIG, acks,
        ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, producerReconnectBackOffMs);
  }

  private StringSerializer producerKeySerializer() {
    return new StringSerializer();
  }

  private JsonSerializer<Object> producerValueSerializer() {
    return new JsonSerializer<>(objectMapper);
  }

  @Bean
  @Primary
  public DefaultKafkaProducerFactory<String, Object> jsonProducerFactory() {
    var defaultKafkaProducerFactory = new DefaultKafkaProducerFactory<>(
        producerConfig(),
        producerKeySerializer(),
        producerValueSerializer());
    defaultKafkaProducerFactory.setTransactionIdPrefix("kafka_tx");

    return defaultKafkaProducerFactory;
  }

  /**
   * Configures the Kafka template to send event.
   *
   * @return the Kafka template
   */
  @Bean
  @Primary
  public KafkaTemplate<String, Object> kafkaTemplate() {
    var template = new KafkaTemplate<>(jsonProducerFactory());
    template.setProducerListener(producerCallback);
    return template;
  }

  @Bean
  public JpaTransactionManager jpaKafkaTransactionManager(EntityManagerFactory entityManager) {
    return new JpaTransactionManager(entityManager);
  }

  @Bean
  public KafkaTransactionManager<String, Object> kafkaTransactionManager() {
    return new KafkaTransactionManager<>(jsonProducerFactory());
  }

  @Bean
  public ChainedKafkaTransactionManager chainedKafkaTransactionManager(
      @Qualifier("jpaKafkaTransactionManager") JpaTransactionManager jpaTransactionManager,
      KafkaTransactionManager kafkaTransactionManager) {
    return new ChainedKafkaTransactionManager(jpaTransactionManager, kafkaTransactionManager);
  }
}
