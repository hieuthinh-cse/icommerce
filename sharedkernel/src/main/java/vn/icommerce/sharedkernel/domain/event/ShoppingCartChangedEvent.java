package vn.icommerce.sharedkernel.domain.event;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This event occurs when a cart is changed.
 */
@Accessors(chain = true)
@Data
public class ShoppingCartChangedEvent implements DomainEvent {

  private Long cartId;
}
