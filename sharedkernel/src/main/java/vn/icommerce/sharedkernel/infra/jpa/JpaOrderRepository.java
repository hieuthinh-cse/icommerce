package vn.icommerce.sharedkernel.infra.jpa;

import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.DomainCode;
import vn.icommerce.sharedkernel.domain.model.Order;
import vn.icommerce.sharedkernel.domain.repository.OrderRepository;

/**
 * Implementation that uses the Jpa/Spring implementation to perform business.
 */
@Slf4j
@Repository
public class JpaOrderRepository implements OrderRepository {

  private final SpringOrderRepository springOrderRepository;

  private final EntityManager entityManager;

  public JpaOrderRepository(
      SpringOrderRepository springOrderRepository,
      EntityManager entityManager) {
    this.springOrderRepository = springOrderRepository;
    this.entityManager = entityManager;
  }

  @Override
  public Order requireById(Long orderId) {
    var order = springOrderRepository.findById(orderId)
        .orElseThrow(() -> new DomainException(DomainCode.BUYER_NOT_FOUND, orderId));

    log.info("method: requireById, cartId: {} , cart: {}", orderId, order);

    return order;
  }


  @Override
  public void create(Order cart) {
    entityManager.persist(cart);

    log.info("method: create, cart: {}", cart);
  }

  @Override
  public Optional<Order> findById(Long orderId) {
    var orderOptional = springOrderRepository.findById(orderId);

    log.info("method: findById, orderId: {} , orderOptional: {}", orderId, orderOptional);

    return orderOptional;
  }
}
