package vn.icommerce.sharedkernel.domain.repository;

import java.util.Optional;
import vn.icommerce.sharedkernel.domain.model.Order;
import vn.icommerce.sharedkernel.domain.model.ShoppingCart;

/**
 * This repository manages the persistence logic of {@link Order} entity.
 */
public interface OrderRepository {

  /**
   * Find a account with a given id
   *
   * @param orderId the cart id to find cart
   * @return the wallet if found
   */
  Order requireById(Long orderId);

  void create(Order order);

  /**
   * Finds the account given the wallet id.
   *
   * @param orderId the wallet id to find
   * @return the account if found
   */
  Optional<Order> findById(Long orderId);

}
