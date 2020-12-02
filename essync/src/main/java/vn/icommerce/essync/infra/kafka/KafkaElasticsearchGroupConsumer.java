/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.essync.infra.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import vn.icommerce.essync.app.ProductConsumer;
import vn.icommerce.sharedkernel.domain.event.ProductCreatedEvent;
import vn.icommerce.sharedkernel.domain.event.ProductUpdatedEvent;

/**
 * Kafka domain event consumer.
 */
@Component
@Slf4j
public class KafkaElasticsearchGroupConsumer {

  private final ProductConsumer productConsumer;

  /**
   * Inject dependent services.
   *
   * @param productConsumer ...
   */
  public KafkaElasticsearchGroupConsumer(ProductConsumer productConsumer) {
    this.productConsumer = productConsumer;
  }

  @KafkaListener(topics = "#{kafkaConfig.productCreatedEventTopicId}")
  public void onProductCreatedEvent(
      ProductCreatedEvent event,
      Acknowledgment acknowledgment) {
    productConsumer.indexById(event.getProductId());
    acknowledgment.acknowledge();
  }

  @KafkaListener(topics = "#{kafkaConfig.productUpdatedEventTopicId}")
  public void onProductUpdatedEvent(
      ProductUpdatedEvent event,
      Acknowledgment acknowledgment) {
    productConsumer.indexById(event.getProductId());
    acknowledgment.acknowledge();
  }
}
