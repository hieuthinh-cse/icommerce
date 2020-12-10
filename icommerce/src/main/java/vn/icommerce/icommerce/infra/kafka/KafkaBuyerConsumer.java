/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.infra.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import vn.icommerce.icommerce.app.cart.CreateCartCmd;
import vn.icommerce.icommerce.app.cart.BuyerShoppingCartAppService;
import vn.icommerce.sharedkernel.domain.event.BuyerCreatedEvent;
import vn.icommerce.sharedkernel.domain.event.OrderCreatedEvent;

/**
 * Kafka domain event consumer.
 */
@Component
@Slf4j
public class KafkaBuyerConsumer {

  private final BuyerShoppingCartAppService buyerShoppingCartAppService;

  /**
   * Inject dependent services.
   * @param buyerShoppingCartAppService ...
   */
  public KafkaBuyerConsumer(
      BuyerShoppingCartAppService buyerShoppingCartAppService) {
    this.buyerShoppingCartAppService = buyerShoppingCartAppService;
  }


  @KafkaListener(topics = "#{kafkaConfig.buyerCreatedEventTopicId}")
  public void onBuyerCreatedEvent(
      BuyerCreatedEvent event,
      Acknowledgment acknowledgment) {
    var cmd = new CreateCartCmd()
        .setBuyerId(event.getBuyerId());

    buyerShoppingCartAppService.createCart(cmd);
    acknowledgment.acknowledge();
  }

  @KafkaListener(topics = "#{kafkaConfig.orderCreatedEventTopicId}")
  public void onOrderCreatedEvent(
      OrderCreatedEvent event,
      Acknowledgment acknowledgment) {
    var cmd = new CreateCartCmd()
        .setBuyerId(event.getBuyerId());

    buyerShoppingCartAppService.createCart(cmd);
    acknowledgment.acknowledge();
  }
}
