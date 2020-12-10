package vn.icommerce.icommerce.app.order;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.icommerce.icommerce.app.component.BuyerInfoHolder;
import vn.icommerce.sharedkernel.app.component.OutboxEngine;
import vn.icommerce.sharedkernel.app.component.TxManager;
import vn.icommerce.sharedkernel.domain.event.OrderCreatedEvent;
import vn.icommerce.sharedkernel.domain.event.ShoppingCartChangedEvent;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.DomainCode;
import vn.icommerce.sharedkernel.domain.model.Order;
import vn.icommerce.sharedkernel.domain.model.OrderStatus;
import vn.icommerce.sharedkernel.domain.model.ShippingAddress;
import vn.icommerce.sharedkernel.domain.model.ShoppingCart;
import vn.icommerce.sharedkernel.domain.model.ShoppingCartItem;
import vn.icommerce.sharedkernel.domain.model.ShoppingCartStatus;
import vn.icommerce.sharedkernel.domain.repository.OrderRepository;
import vn.icommerce.sharedkernel.domain.repository.ProductRepository;
import vn.icommerce.sharedkernel.domain.repository.ShippingAddressRepository;
import vn.icommerce.sharedkernel.domain.repository.ShoppingCartRepository;

@Service
@Slf4j
public class StdBuyerOrderAppService implements BuyerOrderAppService {

  private final ProductRepository productRepository;

  private final ShoppingCartRepository shoppingCartRepository;

  private final OrderRepository orderRepository;

  private final ShippingAddressRepository shippingAddressRepository;

  private final BuyerInfoHolder buyerInfoHolder;

  private final TxManager txManager;

  private final OutboxEngine outboxEngine;

  public StdBuyerOrderAppService(
      ProductRepository productRepository,
      ShoppingCartRepository shoppingCartRepository,
      OrderRepository orderRepository,
      ShippingAddressRepository shippingAddressRepository,
      BuyerInfoHolder buyerInfoHolder,
      TxManager txManager,
      OutboxEngine outboxEngine) {
    this.productRepository = productRepository;
    this.shoppingCartRepository = shoppingCartRepository;
    this.orderRepository = orderRepository;
    this.shippingAddressRepository = shippingAddressRepository;
    this.buyerInfoHolder = buyerInfoHolder;
    this.txManager = txManager;
    this.outboxEngine = outboxEngine;
  }

  @Override
  public CreateOrderDto createOrder(CreateOrderCmd cmd) {
    log.info("method: createOrder, cmd: {}", cmd);
    var buyerId = buyerInfoHolder.getBuyerId();

    var domainEvent = txManager.doInTx(() -> {
//      var address = shippingAddressRepository.requireDefaultByBuyerId(buyerId);

      var cart = shoppingCartRepository.requireCurrentCart(buyerId);

      if (cart.getItems().isEmpty()) {
        throw new DomainException(DomainCode.SHOPPING_CART_EMPTY);
      }

      var change = refreshCart(cart);

      if (change) {
        var event = new ShoppingCartChangedEvent()
            .setCartId(cart.getShoppingCartId());

        outboxEngine.create(event);
        return event;
      }

      var order = new Order()
          .setBuyerId(buyerId)
          .setCart(cart)
          .setStatus(OrderStatus.PROCESSING)
          .setPaymentMethod(cmd.getPaymentMethod())
          .setShippingAddress(
              new ShippingAddress()
                  .setName(cmd.getName())
                  .setRegion(cmd.getRegion())
                  .setPhoneNumber(cmd.getPhoneNumber())
                  .setStreet(cmd.getStreet())
          );

      cart.setStatus(ShoppingCartStatus.COMPLETED);

      orderRepository.create(order);

      var event = new OrderCreatedEvent()
          .setOrderId(order.getOrderId())
          .setBuyerId(buyerId);

      outboxEngine.create(event);
      return event;
    });

    CreateOrderDto dto = new CreateOrderDto();

    if (domainEvent instanceof ShoppingCartChangedEvent) {
      dto.setDomainCode(DomainCode.SHOPPING_CART_CHANGED);
    } else {
      var event = (OrderCreatedEvent) domainEvent;
      dto.setDomainCode(DomainCode.REQUEST_PROCESSED_SUCCESSFULLY)
          .setOrderId(event.getOrderId());
    }

    log.info("method: createOrder, , dto: {}", dto);

    return dto;
  }

  private Boolean refreshCart(ShoppingCart cart) {
    var productIds = cart
        .getItems()
        .stream()
        .map(ShoppingCartItem::getProductId)
        .collect(Collectors.toList());

    var change = false;

    var products = productRepository.findByIdIn(productIds);

    for (var item : cart.getItems()) {
      var product = products.stream()
          .filter((p) -> p.getProductId().equals(item.getProductId()))
          .findFirst()
          .orElseThrow(() -> new DomainException(DomainCode.PRODUCT_NOT_FOUND));

      if (!item.getPrice().equals(product.getProductPrice())) {
        change = true;
      }

      item.setPrice(product.getProductPrice());
    }

    cart.setUpdatedAt(OffsetDateTime.now());
    return change;
  }
}
