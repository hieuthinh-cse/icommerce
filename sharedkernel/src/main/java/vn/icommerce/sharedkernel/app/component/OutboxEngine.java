package vn.icommerce.sharedkernel.app.component;

import vn.icommerce.sharedkernel.domain.event.DomainEvent;

/**
 *
 */
public interface OutboxEngine {

  void create(DomainEvent event);
}
