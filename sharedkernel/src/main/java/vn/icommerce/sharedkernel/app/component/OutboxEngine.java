package vn.icommerce.sharedkernel.app.component;

import vn.icommerce.sharedkernel.domain.event.DomainEvent;

/**
 * @author tuyenpham
 */
public interface OutboxEngine {

  void create(DomainEvent event);
}
