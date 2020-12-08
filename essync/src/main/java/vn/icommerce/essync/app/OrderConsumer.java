package vn.icommerce.essync.app;

/**
 * Standard implementation for the product service.
 */
public interface OrderConsumer {

  /**
   * Indexes the account with the given id.
   *
   * @param orderId the shopping cart id to index
   */
  void indexById(Long orderId);
}
