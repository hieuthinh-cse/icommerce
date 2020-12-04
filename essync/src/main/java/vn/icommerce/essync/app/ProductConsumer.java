package vn.icommerce.essync.app;

/**
 * Standard implementation for the product service.
 */
public interface ProductConsumer {

  /**
   * Indexes the account with the given id.
   *
   * @param productId the product id to index
   */
  void indexById(Long productId);
}
