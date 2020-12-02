package vn.icommerce.icommerce.app.cart;

/**
 * Interface to the service that handles the use case of product business logic.
 */
public interface BuyerShoppingCartAppService {

  Long createCart(CreateCartCmd cmd);

  void addProductCart(AddProduct2CartCmd cmd);

  void updateProductCart(UpdateProductCartCmd cmd);

  void deleteProductCart(DeleteProductCartCmd cmd);
}
