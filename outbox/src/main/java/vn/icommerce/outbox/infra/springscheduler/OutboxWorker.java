package vn.icommerce.outbox.infra.springscheduler;

import static vn.icommerce.common.brave.BraveExecutor.CORRELATION_ID;
import static vn.icommerce.common.brave.BraveExecutor.SPAN_ID;
import static vn.icommerce.common.brave.BraveExecutor.TRACE_ID;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.icommerce.common.brave.BraveExecutor;
import vn.icommerce.common.dedup.CmdProceeded;
import vn.icommerce.common.dedup.JpaCmdProceededRepository;
import vn.icommerce.common.kafka.KafkaExecutor;
import vn.icommerce.common.outbox.JpaOutboxRepository;
import vn.icommerce.common.outbox.Outbox;
import vn.icommerce.common.springtx.SpringTxExecutor;

@Component
@Slf4j
public class OutboxWorker {

  public static final String KAFKA_TOPIC_HEADER = "kafka_topic";

  public static final String EVENT_ID = "event_id";

  private final long retentionDay;

  private final JpaOutboxRepository jpaOutboxRepository;

  private final SpringTxExecutor springTxExecutor;

  private final KafkaExecutor kafkaExecutor;

  private final JpaCmdProceededRepository jpaCmdProceededRepository;

  private final BraveExecutor braveExecutor;

  public OutboxWorker(
      @Value("#{schedulerConfig.retentionDay}") long retentionDay,
      JpaOutboxRepository jpaOutboxRepository,
      SpringTxExecutor springTxExecutor,
      KafkaExecutor kafkaExecutor,
      JpaCmdProceededRepository jpaCmdProceededRepository,
      BraveExecutor braveExecutor) {
    this.retentionDay = retentionDay;
    this.jpaOutboxRepository = jpaOutboxRepository;
    this.springTxExecutor = springTxExecutor;
    this.kafkaExecutor = kafkaExecutor;
    this.jpaCmdProceededRepository = jpaCmdProceededRepository;
    this.braveExecutor = braveExecutor;
  }


  @Scheduled(fixedDelayString = "#{schedulerConfig.timeDelayMs}")
  public void watchEvent() {
    springTxExecutor.doKafkaInTx(() -> {
      var outboxList = jpaOutboxRepository.findTop1000();

      outboxList
          .forEach(outbox -> Optional
              .ofNullable(outbox.getTraceContext())
              .ifPresentOrElse(
                  traceContext -> braveExecutor.run(
                      traceContext.get(TRACE_ID),
                      traceContext.get(SPAN_ID),
                      traceContext.get(CORRELATION_ID),
                      () -> publishMessage(outbox, traceContext.get(CORRELATION_ID))),
                  () -> publishMessage(outbox, "")));

      jpaOutboxRepository.deleteAll(outboxList);
    });

    log.info("method: watchEvent");
  }

  private void publishMessage(Outbox outbox, String correlationId) {
    var message = MessageBuilder
        .withPayload(outbox.getPayload())
        .setHeader(KAFKA_TOPIC_HEADER, outbox.getTopic())
        .setHeader(EVENT_ID, outbox.getEventId())
        .setHeader(TRACE_ID, MDC.get(TRACE_ID))
        .setHeader(SPAN_ID, MDC.get(SPAN_ID))
        .setHeader(CORRELATION_ID, correlationId)
        .build();

    kafkaExecutor.publish(message);
  }

  @Scheduled(cron = "#{schedulerConfig.clearTrashCron}")
  public void clearTrash() {
    springTxExecutor.doInTx(() -> {
      List<CmdProceeded> cmdProceedList;
      do {
        cmdProceedList = jpaCmdProceededRepository.findTop1000OutDated(retentionDay);
        jpaCmdProceededRepository.deleteAll(cmdProceedList);
      } while (!cmdProceedList.isEmpty());
    });

    log.info("method: clearTrash");
  }
}
