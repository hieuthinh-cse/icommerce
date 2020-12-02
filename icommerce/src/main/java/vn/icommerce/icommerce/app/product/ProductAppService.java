package vn.icommerce.icommerce.app.product;

/**
 * Interface to the service that handles the use case of product business logic.
 */
public interface ProductAppService {

  Long createProduct(CreateProductCmd cmd);

  void updateProduct(UpdateProductCmd cmd);
}
