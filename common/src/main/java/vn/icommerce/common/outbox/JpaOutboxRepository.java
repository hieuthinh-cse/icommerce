/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.outbox;

import static vn.icommerce.common.brave.BraveExecutor.CORRELATION_ID;
import static vn.icommerce.common.brave.BraveExecutor.SPAN_ID;
import static vn.icommerce.common.brave.BraveExecutor.TRACE_ID;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Repository;

/**
 * Implementation that uses the Jpa/Spring implementation to perform outbox consumed business.
 *
 * <p>Created on 9/06/19.
 *
 * @author vanlh
 */
@Slf4j
@Repository
public class JpaOutboxRepository {

  private final EntityManager entityManager;

  private final SpringOutboxRepository springOutboxRepository;

  public JpaOutboxRepository(EntityManager entityManager,
      SpringOutboxRepository springOutboxRepository) {
    this.entityManager = entityManager;
    this.springOutboxRepository = springOutboxRepository;
  }

  public void create(Outbox outbox) {
    var traceId = Optional
        .ofNullable(MDC.get(TRACE_ID))
        .orElse("");

    var spanId = Optional
        .ofNullable(MDC.get(SPAN_ID))
        .orElse("");

    var correlationId = Optional
        .ofNullable(MDC.get(CORRELATION_ID))
        .orElse("");

    outbox.setTraceContext(Map.of(
        TRACE_ID, traceId,
        SPAN_ID, spanId,
        CORRELATION_ID, correlationId));

    entityManager.persist(outbox);

    log.info("method: create, outbox: {}", outbox);
  }

  public List<Outbox> findTop1000() {
    var outboxes = springOutboxRepository.findTop1000ByOrderByCreatedAt();

    log.info("method: findTop1000, outboxesSize: {}", outboxes.size());

    return outboxes;
  }

  public void deleteAll(Collection<Outbox> outboxes) {
    springOutboxRepository.deleteAll(outboxes);

    log.info("method: deleteAll, outboxesSize: {}", outboxes.size());
  }
}
