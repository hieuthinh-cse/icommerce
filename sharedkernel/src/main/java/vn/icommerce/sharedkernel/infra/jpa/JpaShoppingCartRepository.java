package vn.icommerce.sharedkernel.infra.jpa;

import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.DomainCode;
import vn.icommerce.sharedkernel.domain.model.ShoppingCart;
import vn.icommerce.sharedkernel.domain.model.ShoppingCartStatus;
import vn.icommerce.sharedkernel.domain.repository.ShoppingCartRepository;

/**
 * Implementation that uses the Jpa/Spring implementation to perform business.
 */
@Slf4j
@Repository
public class JpaShoppingCartRepository implements ShoppingCartRepository {

  private final SpringShoppingCartRepository springShoppingCartRepository;

  private final EntityManager entityManager;

  public JpaShoppingCartRepository(SpringShoppingCartRepository springShoppingCartRepository,
      EntityManager entityManager) {
    this.springShoppingCartRepository = springShoppingCartRepository;
    this.entityManager = entityManager;
  }

  @Override
  public ShoppingCart requireById(Long cartId) {
    var cart = springShoppingCartRepository.findById(cartId)
        .orElseThrow(() -> new DomainException(DomainCode.BUYER_NOT_FOUND, cartId));

    log.info("method: requireById, cartId: {} , cart: {}", cartId, cart);

    return cart;
  }

  @Override
  public Optional<ShoppingCart> findByBuyerIdAndStatus(Long buyerId, ShoppingCartStatus status) {
    var cart = springShoppingCartRepository.findByBuyerIdAndStatus(buyerId, status);

    log.info("method: findByBuyerIdAndStatus, buyerId: {} , cart: {}", buyerId, cart);

    return cart;
  }

  @Override
  public ShoppingCart requireCurrentCart(Long buyerId) {
    var cart = springShoppingCartRepository.findByBuyerIdAndStatus(buyerId, ShoppingCartStatus.PROCESSING)
        .orElseThrow(() -> new DomainException(DomainCode.PROCESSING_CART_NOT_FOUND, buyerId));

    log.info("method: requireCurrentCart, buyerId: {} , cart: {}", buyerId, cart);

    return cart;
  }


  @Override
  public void create(ShoppingCart cart) {
    entityManager.persist(cart);

    log.info("method: create, cart: {}", cart);
  }

  @Override
  public Optional<ShoppingCart> findById(Long cartId) {
    var cartOptional = springShoppingCartRepository.findById(cartId);

    log.info("method: findById, cartId: {} , cartOptional: {}", cartId, cartOptional);

    return cartOptional;
  }
}
