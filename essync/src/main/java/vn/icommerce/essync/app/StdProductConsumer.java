package vn.icommerce.essync.app;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.icommerce.sharedkernel.app.component.SearchEngine;
import vn.icommerce.sharedkernel.domain.repository.ProductRepository;

/**
 * Standard implementation for the product service.
 */
@Slf4j
@Component
public class StdProductConsumer implements ProductConsumer {

  private static final String PRODUCT_INDEX = "product";

  private static final String PRODUCT_HISTORY_INDEX = "product_history";

  private final ProductRepository productRepository;

  private final SearchEngine searchEngine;

  /**
   * Constructor to inject dependencies.
   */
  public StdProductConsumer(
      ProductRepository productRepository,
      SearchEngine searchEngine) {
    this.productRepository = productRepository;
    this.searchEngine = searchEngine;
  }

  /**
   * Indexes the product with the given id.
   *
   * @param productId the product id to index
   */
  @Transactional(readOnly = true)
  public void indexById(Long productId) {
    productRepository
        .findById(productId)
        .ifPresent(product -> {
          searchEngine.index(
              PRODUCT_INDEX,
              product.getProductId().toString(),
              product.getUpdatedAt().toInstant().toEpochMilli(),
              product);

          searchEngine.index(
              PRODUCT_HISTORY_INDEX,
              UUID.randomUUID().toString(),
              product.getUpdatedAt().toInstant().toEpochMilli(),
              product);
        });

    log.info("method: indexById, productId: {}", productId);
  }
}
