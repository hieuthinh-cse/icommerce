package vn.icommerce.sharedkernel.domain.event;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This event occurs when an buyer account is created.
 */
@Accessors(chain = true)
@Data
public class BuyerCreatedEvent implements DomainEvent {

  private Long buyerId;
}
