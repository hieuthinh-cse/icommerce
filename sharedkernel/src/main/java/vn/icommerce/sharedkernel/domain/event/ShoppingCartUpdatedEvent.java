

package vn.icommerce.sharedkernel.domain.event;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This event occurs when a cart is updated.
 */
@Accessors(chain = true)
@Data
public class ShoppingCartUpdatedEvent implements DomainEvent {

  private Long cartId;
}
