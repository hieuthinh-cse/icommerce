package vn.icommerce.essync.app;

import java.util.UUID;

/**
 * Standard implementation for the product service.
 *
 */
public interface BuyerConsumer {

  /**
   * Indexes the account with the given id.
   *
   * @param productId the product id to index
   */
  void indexById(Long productId);
}
