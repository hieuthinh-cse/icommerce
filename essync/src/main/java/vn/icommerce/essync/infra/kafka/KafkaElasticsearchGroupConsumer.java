package vn.icommerce.essync.infra.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import vn.icommerce.essync.app.OrderConsumer;
import vn.icommerce.essync.app.ProductConsumer;
import vn.icommerce.essync.app.ShippingAddressConsumer;
import vn.icommerce.essync.app.ShoppingCartConsumer;
import vn.icommerce.sharedkernel.domain.event.OrderCreatedEvent;
import vn.icommerce.sharedkernel.domain.event.ProductCreatedEvent;
import vn.icommerce.sharedkernel.domain.event.ProductUpdatedEvent;
import vn.icommerce.sharedkernel.domain.event.ShippingAddressCreatedEvent;
import vn.icommerce.sharedkernel.domain.event.ShoppingCartChangedEvent;
import vn.icommerce.sharedkernel.domain.event.ShoppingCartCreatedEvent;
import vn.icommerce.sharedkernel.domain.event.ShoppingCartUpdatedEvent;

/**
 * Kafka domain event consumer.
 */
@Component
@Slf4j
public class KafkaElasticsearchGroupConsumer {

  private final ProductConsumer productConsumer;

  private final ShoppingCartConsumer shoppingCartConsumer;

  private final OrderConsumer orderConsumer;

  private final ShippingAddressConsumer shippingAddressConsumer;

  /**
   * Inject dependent services.
   *
   * @param productConsumer         ...
   * @param shoppingCartConsumer    ...
   * @param orderConsumer           ...
   * @param shippingAddressConsumer ...
   */
  public KafkaElasticsearchGroupConsumer(
      ProductConsumer productConsumer,
      ShoppingCartConsumer shoppingCartConsumer,
      OrderConsumer orderConsumer,
      ShippingAddressConsumer shippingAddressConsumer) {
    this.productConsumer = productConsumer;
    this.shoppingCartConsumer = shoppingCartConsumer;
    this.orderConsumer = orderConsumer;
    this.shippingAddressConsumer = shippingAddressConsumer;
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

  @KafkaListener(topics = "#{kafkaConfig.shoppingCartChangedEventTopicId}")
  public void onShoppingCartChangedEvent(
      ShoppingCartChangedEvent event,
      Acknowledgment acknowledgment) {
    shoppingCartConsumer.indexById(event.getCartId());
    acknowledgment.acknowledge();
  }

  @KafkaListener(topics = "#{kafkaConfig.orderCreatedEventTopicId}")
  public void onOrderCreatedEvent(
      OrderCreatedEvent event,
      Acknowledgment acknowledgment) {
    orderConsumer.indexById(event.getOrderId());
    acknowledgment.acknowledge();
  }

  @KafkaListener(topics = "#{kafkaConfig.shippingAddressCreatedEventTopicId}")
  public void onShippingAddressCreatedEvent(
      ShippingAddressCreatedEvent event,
      Acknowledgment acknowledgment) {
    shippingAddressConsumer.indexById(event.getAddressId());
    acknowledgment.acknowledge();
  }
}
