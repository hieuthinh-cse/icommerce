package vn.icommerce.sharedkernel.domain.event;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This event occurs when an shipping address is created.
 */
@Accessors(chain = true)
@Data
public class ShippingAddressCreatedEvent implements DomainEvent {

  private UUID addressId;
}
