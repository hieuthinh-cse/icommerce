package vn.icommerce.icommerce.app.cart;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class DeleteProductCartCmd {

  @NotNull
  private Long productId;
}
