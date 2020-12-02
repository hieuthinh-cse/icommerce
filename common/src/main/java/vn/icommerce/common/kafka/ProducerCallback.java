/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.kafka;

import static vn.icommerce.common.brave.BraveExecutor.CORRELATION_ID;
import static vn.icommerce.common.brave.BraveExecutor.SPAN_ID;
import static vn.icommerce.common.brave.BraveExecutor.TRACE_ID;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;
import vn.icommerce.common.brave.BraveExecutor;

@Slf4j
@Component
public class ProducerCallback implements ProducerListener<String, Object> {

  private final BraveExecutor braveExecutor;

  public ProducerCallback(BraveExecutor braveExecutor) {
    this.braveExecutor = braveExecutor;
  }

  private String getHeaderValue(ProducerRecord producerRecord, String headerKey) {
    return Optional
        .ofNullable(producerRecord.headers().lastHeader(headerKey))
        .map(header -> new String(header.value()))
        .orElse("");
  }

  @Override
  public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
    braveExecutor.run(
        getHeaderValue(producerRecord, TRACE_ID),
        getHeaderValue(producerRecord, SPAN_ID),
        getHeaderValue(producerRecord, CORRELATION_ID),
        () -> log.info("method: onSuccess, value: {} , topic: {} , key: {} , partition: {} ",
            producerRecord.value(),
            producerRecord.topic(),
            producerRecord.key(),
            producerRecord.partition()));
  }

  @Override
  public void onError(ProducerRecord producerRecord, Exception exception) {
    braveExecutor.run(
        getHeaderValue(producerRecord, TRACE_ID),
        getHeaderValue(producerRecord, SPAN_ID),
        getHeaderValue(producerRecord, CORRELATION_ID),
        () -> log.error("method: onError, value: {} , topic: {} , key: {} , partition: {} ",
            producerRecord.value(),
            producerRecord.topic(),
            producerRecord.key(),
            producerRecord.partition(),
            exception));
  }
}
