/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.sharedkernel.domain.repository;

import java.util.Optional;
import vn.icommerce.sharedkernel.domain.model.ShoppingCart;
import vn.icommerce.sharedkernel.domain.model.ShoppingCartStatus;

/**
 * This repository manages the persistence logic of {@link ShoppingCart} entity.
 *
 */
public interface ShoppingCartRepository {

  /**
   * Find a account with a given id
   *
   * @param cartId the cart id to find cart
   * @return the wallet if found
   */
  ShoppingCart requireById(Long cartId);

  Optional<ShoppingCart> findByBuyerIdAndStatus(Long buyerId, ShoppingCartStatus status);

  ShoppingCart requireCurrentCart(Long buyerId);

  /**
   * Saves the account to the persistence store.
   *
   * @param account the account to save
   */
  void create(ShoppingCart account);

  /**
   * Finds the account given the wallet id.
   *
   * @param cartId the wallet id to find
   * @return the account if found
   */
  Optional<ShoppingCart> findById(Long cartId);

}
