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
import vn.icommerce.essync.app.BuyerConsumer;
import vn.icommerce.essync.app.ProductConsumer;
import vn.icommerce.essync.app.ShoppingCartConsumer;
import vn.icommerce.sharedkernel.domain.event.BuyerCreatedEvent;
import vn.icommerce.sharedkernel.domain.event.ProductCreatedEvent;
import vn.icommerce.sharedkernel.domain.event.ProductUpdatedEvent;
import vn.icommerce.sharedkernel.domain.event.ShoppingCartCreatedEvent;
import vn.icommerce.sharedkernel.domain.event.ShoppingCartUpdatedEvent;

/**
 * Kafka domain event consumer.
 */
@Component
@Slf4j
public class KafkaElasticsearchGroupConsumer {

  private final ProductConsumer productConsumer;

  private final BuyerConsumer buyerConsumer;

  private final ShoppingCartConsumer shoppingCartConsumer;

  /**
   * Inject dependent services.
   *
   * @param productConsumer      ...
   * @param buyerConsumer        ...
   * @param shoppingCartConsumer ...
   */
  public KafkaElasticsearchGroupConsumer(ProductConsumer productConsumer,
      BuyerConsumer buyerConsumer,
      ShoppingCartConsumer shoppingCartConsumer) {
    this.productConsumer = productConsumer;
    this.buyerConsumer = buyerConsumer;
    this.shoppingCartConsumer = shoppingCartConsumer;
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

  @KafkaListener(topics = "#{kafkaConfig.buyerCreatedEventTopicId}")
  public void onBuyerCreatedEvent(
      BuyerCreatedEvent event,
      Acknowledgment acknowledgment) {
    buyerConsumer.indexById(event.getBuyerId());
    acknowledgment.acknowledge();
  }

  @KafkaListener(topics = "#{kafkaConfig.shoppingCartCreatedEventTopicId}")
  public void onShoppingCartCreatedEvent(
      ShoppingCartCreatedEvent event,
      Acknowledgment acknowledgment) {
    shoppingCartConsumer.indexById(event.getCartId());
    acknowledgment.acknowledge();
  }

  @KafkaListener(topics = "#{kafkaConfig.shoppingCartUpdatedEventTopicId}")
  public void onShoppingCartUpdatedEvent(
      ShoppingCartUpdatedEvent event,
      Acknowledgment acknowledgment) {
    shoppingCartConsumer.indexById(event.getCartId());
    acknowledgment.acknowledge();
  }
}
