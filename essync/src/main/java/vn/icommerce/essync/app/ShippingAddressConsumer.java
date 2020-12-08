package vn.icommerce.essync.app;

import java.util.UUID;

/**
 * Standard implementation for the product service.
 */
public interface ShippingAddressConsumer {

  /**
   * Indexes the account with the given id.
   *
   * @param addressId the product id to index
   */
  void indexById(UUID addressId);
}
