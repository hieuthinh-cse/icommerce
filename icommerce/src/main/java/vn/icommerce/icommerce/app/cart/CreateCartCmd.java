package vn.icommerce.icommerce.app.cart;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Command object that has all the information to create a cart.
 */
@Accessors(chain = true)
@Data
public class CreateCartCmd {

  private Long buyerId;
}
