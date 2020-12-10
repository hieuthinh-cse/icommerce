

package vn.icommerce.essync.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.icommerce.sharedkernel.app.component.SearchEngine;
import vn.icommerce.sharedkernel.domain.repository.OrderRepository;

/**
 * Standard implementation for the account service.
 *
 */
@Slf4j
@Component
public class StdOrderConsumer implements OrderConsumer {

  private static final String SHOPPING_CART_INDEX = "order";

  private final OrderRepository orderRepository;

  private final SearchEngine searchEngine;

  /**
   * Constructor to inject dependencies.
   */
  public StdOrderConsumer(
      OrderRepository orderRepository,
      SearchEngine searchEngine) {
    this.orderRepository = orderRepository;
    this.searchEngine = searchEngine;
  }

  /**
   * Indexes the product with the given id.
   *
   * @param orderId the order id to index
   */
  @Transactional(readOnly = true)
  public void indexById(Long orderId) {
    orderRepository
        .findById(orderId)
        .ifPresent(order -> searchEngine.index(
            SHOPPING_CART_INDEX,
            order.getOrderId().toString(),
            order.getUpdatedAt().toInstant().toEpochMilli(),
            order));

    log.info("method: indexById, orderId: {}", orderId);
  }
}
