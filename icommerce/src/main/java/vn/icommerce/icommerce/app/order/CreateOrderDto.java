package vn.icommerce.icommerce.app.order;

import lombok.Data;
import lombok.experimental.Accessors;
import vn.icommerce.sharedkernel.domain.model.DomainCode;

/**
 * This object represents the result of a create order.
 */
@Data
@Accessors(chain = true)
public class CreateOrderDto {

  private Long orderId;

  private DomainCode domainCode;
}
