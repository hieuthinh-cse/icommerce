package vn.icommerce.icommerce.app.order;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import vn.icommerce.icommerce.app.validator.PhoneConstraint;
import vn.icommerce.sharedkernel.domain.model.PaymentMethod;

/**
 * This object represents the result of a create order.
 */
@Data
@Accessors(chain = true)
public class CreateOrderCmd {

  @NotNull
  private PaymentMethod paymentMethod;

  @NotBlank
  private String name;

  @NotBlank
  @PhoneConstraint
  private String phoneNumber;

  @NotBlank
  private String region;

  @NotBlank
  private String street;
}
