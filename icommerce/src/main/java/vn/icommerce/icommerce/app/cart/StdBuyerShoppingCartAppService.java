package vn.icommerce.icommerce.app.cart;

import java.time.OffsetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.icommerce.icommerce.app.component.BuyerInfoHolder;
import vn.icommerce.sharedkernel.app.component.OutboxEngine;
import vn.icommerce.sharedkernel.app.component.TxManager;
import vn.icommerce.sharedkernel.domain.event.ShoppingCartCreatedEvent;
import vn.icommerce.sharedkernel.domain.event.ShoppingCartUpdatedEvent;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.DomainCode;
import vn.icommerce.sharedkernel.domain.model.ShoppingCart;
import vn.icommerce.sharedkernel.domain.model.ShoppingCartItem;
import vn.icommerce.sharedkernel.domain.model.ShoppingCartStatus;
import vn.icommerce.sharedkernel.domain.repository.BuyerRepository;
import vn.icommerce.sharedkernel.domain.repository.ProductRepository;
import vn.icommerce.sharedkernel.domain.repository.ShoppingCartRepository;

@Service
@Slf4j
public class StdBuyerShoppingCartAppService implements BuyerShoppingCartAppService {

  private final ProductRepository productRepository;

  private final ShoppingCartRepository shoppingCartRepository;

  private final BuyerRepository buyerRepository;

  private final BuyerInfoHolder buyerInfoHolder;

  private final TxManager txManager;

  private final OutboxEngine outboxEngine;

  public StdBuyerShoppingCartAppService(
      ProductRepository productRepository,
      ShoppingCartRepository shoppingCartRepository,
      BuyerRepository buyerRepository,
      BuyerInfoHolder buyerInfoHolder, TxManager txManager,
      OutboxEngine outboxEngine
  ) {
    this.productRepository = productRepository;
    this.shoppingCartRepository = shoppingCartRepository;
    this.buyerRepository = buyerRepository;
    this.buyerInfoHolder = buyerInfoHolder;
    this.txManager = txManager;
    this.outboxEngine = outboxEngine;
  }

  @Override
  public Long createCart(CreateCartCmd cmd) {

    var shoppingCart = txManager.doInTx(() -> {
      buyerRepository.requireById(cmd.getBuyerId());

      shoppingCartRepository
          .findByBuyerIdAndStatus(cmd.getBuyerId(), ShoppingCartStatus.PROCESSING)
          .ifPresent((c) -> {
            throw new DomainException(DomainCode.PROCESSING_CART_EXISTING, c.getShoppingCartId());
          });

      var cart = new ShoppingCart()
          .setBuyerId(cmd.getBuyerId())
          .setStatus(ShoppingCartStatus.PROCESSING);

      shoppingCartRepository.create(cart);

      outboxEngine.create(
          new ShoppingCartCreatedEvent()
              .setCartId(cart.getShoppingCartId())
      );

      return cart;
    });
    return shoppingCart.getShoppingCartId();
  }

  @Override
  public void addProductCart(AddProduct2CartCmd cmd) {
    var buyerId = buyerInfoHolder.getBuyerId();

    txManager.doInTx(() -> {
      var product = productRepository.requireById(cmd.getProductId());

      var cart = shoppingCartRepository.requireCurrentCart(buyerId);

      var count = cart.getItems()
          .stream()
          .filter(item -> item.getProductId().equals(cmd.getProductId()))
          .count();

      if (count >= 1) {
        for (var item : cart.getItems()) {
          if (item.getProductId().equals(cmd.getProductId())) {
            item
                .setPrice(product.getProductPrice())
                .setQuantity(cmd.getQuantity() + item.getQuantity());
          }
        }
      } else {
        var item = new ShoppingCartItem()
            .setShoppingCart(cart)
            .setProductId(product.getProductId())
            .setQuantity(cmd.getQuantity())
            .setPrice(product.getProductPrice())
            .setShoppingCart(cart);
        cart.getItems().add(item);
      }

      cart.setUpdatedAt(OffsetDateTime.now());

      outboxEngine.create(
          new ShoppingCartUpdatedEvent()
              .setCartId(cart.getShoppingCartId())
      );
    });
  }

  @Override
  public void updateProductCart(UpdateProductCartCmd cmd) {
    var buyerId = buyerInfoHolder.getBuyerId();

    txManager.doInTx(() -> {
      buyerRepository.requireById(buyerId);

      var cart = shoppingCartRepository.requireCurrentCart(buyerId);

      for (var item : cart.getItems()) {
        if (item.getCartItemId().equals(cmd.getCartItemId())) {
          item.setQuantity(cmd.getQuantity());
        }
      }

      cart.setUpdatedAt(OffsetDateTime.now());

      outboxEngine.create(
          new ShoppingCartUpdatedEvent()
              .setCartId(cart.getShoppingCartId())
      );

      return cart;
    });
  }


  @Override
  public void deleteProductCart(DeleteProductCartCmd cmd) {
    var buyerId = buyerInfoHolder.getBuyerId();

    txManager.doInTx(() -> {
      buyerRepository.requireById(buyerId);

      var cart = shoppingCartRepository.requireCurrentCart(buyerId);

      cart.getItems()
          .removeIf(item -> item.getProductId().equals(cmd.getProductId()));

      outboxEngine.create(
          new ShoppingCartUpdatedEvent()
              .setCartId(cart.getShoppingCartId())
      );

      return cart;
    });
  }
}
