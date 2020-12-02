/*
 * Copyright 2020 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.essync.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.icommerce.sharedkernel.app.component.SearchEngine;
import vn.icommerce.sharedkernel.domain.repository.ProductRepository;

/**
 * Standard implementation for the account service.
 *
 * <p>Created on 8/20/19.
 *
 * @author khoanguyenminh
 */
@Slf4j
@Component
public class StdProductConsumer implements ProductConsumer {

  private static final String ACCOUNT_INDEX = "product";

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
        .ifPresent(product -> searchEngine.index(
            ACCOUNT_INDEX,
            product.getProductId().toString(),
            product.getUpdatedAt().toInstant().toEpochMilli(),
            product));

    log.info("method: indexById, accountId: {}", productId);
  }
}
