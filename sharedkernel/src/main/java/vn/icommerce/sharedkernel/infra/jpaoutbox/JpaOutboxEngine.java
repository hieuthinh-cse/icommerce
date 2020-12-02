package vn.icommerce.sharedkernel.infra.jpaoutbox;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.icommerce.common.jackson.JacksonExecutor;
import vn.icommerce.common.outbox.JpaOutboxRepository;
import vn.icommerce.common.outbox.Outbox;
import vn.icommerce.sharedkernel.app.component.OutboxEngine;
import vn.icommerce.sharedkernel.domain.event.DomainEvent;

/**
 * @author tuyenpham
 */
@Slf4j
@Component
public class JpaOutboxEngine implements OutboxEngine {

  private final JpaOutboxRepository jpaOutboxRepository;

  private final JacksonExecutor jacksonExecutor;

  public JpaOutboxEngine(
      JpaOutboxRepository jpaOutboxRepository,
      JacksonExecutor jacksonExecutor) {
    this.jpaOutboxRepository = jpaOutboxRepository;
    this.jacksonExecutor = jacksonExecutor;
  }

  @Override
  public void create(DomainEvent event) {
    jpaOutboxRepository.create(
        new Outbox()
            .setTopic(event.getClass().getSimpleName())
            .setPayload(jacksonExecutor.serializeAsString(event)));
  }

}
