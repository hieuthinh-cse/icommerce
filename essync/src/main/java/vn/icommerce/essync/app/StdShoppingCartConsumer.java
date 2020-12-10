

package vn.icommerce.essync.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.icommerce.sharedkernel.app.component.SearchEngine;
import vn.icommerce.sharedkernel.domain.repository.ShoppingCartRepository;

/**
 * Standard implementation for the account service.
 *
 */
@Slf4j
@Component
public class StdShoppingCartConsumer implements ShoppingCartConsumer {

  private static final String SHOPPING_CART_INDEX = "shopping_cart";

  private final ShoppingCartRepository shoppingCartRepository;

  private final SearchEngine searchEngine;

  /**
   * Constructor to inject dependencies.
   */
  public StdShoppingCartConsumer(
      ShoppingCartRepository shoppingCartRepository,
      SearchEngine searchEngine) {
    this.shoppingCartRepository = shoppingCartRepository;
    this.searchEngine = searchEngine;
  }

  /**
   * Indexes the product with the given id.
   *
   * @param cartId the cart id to index
   */
  @Transactional(readOnly = true)
  public void indexById(Long cartId) {
    shoppingCartRepository
        .findById(cartId)
        .ifPresent(cart -> searchEngine.index(
            SHOPPING_CART_INDEX,
            cart.getBuyerId().toString(),
            cart.getUpdatedAt().toInstant().toEpochMilli(),
            cart));

    log.info("method: indexById, cartId: {}", cartId);
  }
}
