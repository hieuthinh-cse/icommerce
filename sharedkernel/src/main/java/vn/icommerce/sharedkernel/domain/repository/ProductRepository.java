package vn.icommerce.sharedkernel.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import vn.icommerce.sharedkernel.domain.model.Product;

/**
 * This repository manages the persistence logic of {@link Product} entity.
 */
public interface ProductRepository {

  /**
   * Find a account with a given id
   *
   * @param productId the phone number to find account
   * @return the wallet if found
   */
  Product requireById(Long productId);

  /**
   * Saves the account to the persistence store.
   *
   * @param account the account to save
   */
  void create(Product account);

  /**
   * Finds the account given the wallet id.
   *
   * @param productId the wallet id to find
   * @return the account if found
   */
  Optional<Product> findById(Long productId);

  List<Product> findByIdIn(List<Long> productIds);
}
