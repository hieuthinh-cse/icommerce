package vn.icommerce.essync.app;

/**
 * Standard implementation for the product service.
 */
public interface ShoppingCartConsumer {

  /**
   * Indexes the account with the given id.
   *
   * @param cartId the shopping cart id to index
   */
  void indexById(Long cartId);
}
