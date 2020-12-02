package vn.icommerce.icommerce.app.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.icommerce.sharedkernel.app.component.OutboxEngine;
import vn.icommerce.sharedkernel.app.component.TxManager;
import vn.icommerce.sharedkernel.domain.event.ProductCreatedEvent;
import vn.icommerce.sharedkernel.domain.event.ProductUpdatedEvent;
import vn.icommerce.sharedkernel.domain.model.Product;
import vn.icommerce.sharedkernel.domain.repository.ProductRepository;

@Service
@Slf4j
public class StdProductAppService implements ProductAppService {

  private final ProductRepository productRepository;

  private final TxManager txManager;

  private final OutboxEngine outboxEngine;

  public StdProductAppService(
      ProductRepository productRepository,
      TxManager txManager,
      OutboxEngine outboxEngine
  ) {
    this.productRepository = productRepository;
    this.txManager = txManager;
    this.outboxEngine = outboxEngine;
  }

  @Override
  public Long createProduct(CreateProductCmd cmd) {
    log.info("method: createProduct, cmd: {}", cmd);

    var productId = txManager.doInTx(() -> {

      var product = new Product()
          .setProductName(cmd.getProductName())
          .setProductPrice(cmd.getProductPrice());

      productRepository.create(product);

      var event = new ProductCreatedEvent()
          .setProductId(product.getProductId());
      outboxEngine.create(event);

      return product.getProductId();

    });

    log.info("method: createProduct, productId: {}", productId);

    return productId;
  }

  @Override
  public void updateProduct(UpdateProductCmd cmd) {
    log.info("method: updateProduct, cmd: {}", cmd);

    txManager.doInTx(() -> {
      var product = productRepository
          .requireById(cmd.getProductId());

      var oldPrice = product.getProductPrice();

      product
          .setProductPrice(cmd.getProductPrice())
          .setProductName(cmd.getProductName());

      var event = new ProductUpdatedEvent()
          .setProductId(product.getProductId())
          .setNewPrice(cmd.getProductPrice())
          .setOldPrice(oldPrice);
      outboxEngine.create(event);
    });

    log.info("method: updateProduct");
  }
}
