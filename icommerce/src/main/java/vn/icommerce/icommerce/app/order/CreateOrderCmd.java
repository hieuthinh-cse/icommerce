package vn.icommerce.icommerce.app.order;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import vn.icommerce.sharedkernel.domain.model.PaymentMethod;

/**
 * This object represents the result of a create order.
 */
@Data
@Accessors(chain = true)
public class CreateOrderCmd {

  @NotNull
  private PaymentMethod paymentMethod;
}
