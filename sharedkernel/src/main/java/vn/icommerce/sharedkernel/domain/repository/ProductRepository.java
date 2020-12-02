/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.sharedkernel.domain.repository;

import java.util.Optional;
import vn.icommerce.sharedkernel.domain.model.Product;

/**
 * This repository manages the persistence logic of {@link Product} entity.
 */
public interface ProductRepository {

  /**
   * Find a account with a given id
   *
   * @param accountId the phone number to find account
   * @return the wallet if found
   */
  Product requireById(Long accountId);

  /**
   * Saves the account to the persistence store.
   *
   * @param account the account to save
   */
  void create(Product account);

  /**
   * Finds the account given the wallet id.
   *
   * @param accountId the wallet id to find
   * @return the account if found
   */
  Optional<Product> findById(Long accountId);

}
