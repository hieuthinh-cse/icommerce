/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.sharedkernel.domain.repository;

import java.util.Optional;
import vn.icommerce.sharedkernel.domain.model.Buyer;

/**
 * This repository manages the persistence logic of {@link Buyer} entity.
 *
 */
public interface BuyerRepository {

  /**
   * Find a account with a given id
   *
   * @param buyerId the phone number to find account
   * @return the wallet if found
   */
  Buyer requireById(Long buyerId);

  /**
   * Find a account with a given email
   *
   * @param email the phone number to find account
   * @return the wallet if found
   */
  Optional<Buyer> findByEmail(String email);

  boolean existsByEmail(String email);

  /**
   * Saves the buyer to the persistence store.
   *
   * @param buyer the account to save
   */
  void create(Buyer buyer);

  /**
   * Finds the buyer given the wallet id.
   *
   * @param buyerId the wallet id to find
   * @return the account if found
   */
  Optional<Buyer> findById(Long buyerId);
}
