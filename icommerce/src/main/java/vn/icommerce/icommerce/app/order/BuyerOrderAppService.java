package vn.icommerce.icommerce.app.order;

import vn.icommerce.icommerce.app.cart.AddProduct2CartCmd;
import vn.icommerce.icommerce.app.cart.CreateCartCmd;
import vn.icommerce.icommerce.app.cart.DeleteProductCartCmd;
import vn.icommerce.icommerce.app.cart.UpdateProductCartCmd;

/**
 * Interface to the service that handles the use case of product business logic.
 */
public interface BuyerOrderAppService {

  CreateOrderDto createOrder(CreateOrderCmd cmd);
}
