

package vn.icommerce.sharedkernel.domain.event;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This event occurs when a product is created.
 *
 */
@Accessors(chain = true)
@Data
public class ProductCreatedEvent implements DomainEvent {

  private Long productId;

}
