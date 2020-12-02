/*
 * Copyright 2020 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.springretry;

import java.io.PrintWriter;
import java.io.StringWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.stereotype.Component;
import vn.icommerce.common.jackson.JacksonExecutor;
import vn.icommerce.common.outbox.JpaOutboxRepository;
import vn.icommerce.common.outbox.Outbox;
import vn.icommerce.common.springtx.SpringTxExecutor;

/**
 * <p>Created on 4/26/20.
 *
 * @author khoanguyenminh
 */
@Slf4j
@Component
public class NotificationRetryListener extends RetryListenerSupport {

  private final JpaOutboxRepository jpaOutboxRepository;

  private final JacksonExecutor jacksonExecutor;

  private final SpringRetryConfig springRetryConfig;

  private final SpringTxExecutor springTxExecutor;

  public NotificationRetryListener(
      JpaOutboxRepository jpaOutboxRepository,
      JacksonExecutor jacksonExecutor,
      SpringRetryConfig springRetryConfig,
      SpringTxExecutor springTxExecutor) {
    this.jpaOutboxRepository = jpaOutboxRepository;
    this.jacksonExecutor = jacksonExecutor;
    this.springRetryConfig = springRetryConfig;
    this.springTxExecutor = springTxExecutor;
  }

  @Override
  public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
      Throwable throwable) {
    if (context.getRetryCount() % springRetryConfig.getNotificationThreshold() == 0) {
      var event = new RetryNotificationThresholdReachedEvent()
          .setRetryCount(context.getRetryCount())
          .setRetryNotificationThreshold(springRetryConfig.getNotificationThreshold())
          .setStackTrace(getStackTraceAsString(throwable));

      springTxExecutor.doInNewTx(() -> jpaOutboxRepository.create(
          new Outbox()
              .setTopic(event.getClass().getSimpleName())
              .setPayload(jacksonExecutor.serializeAsString(event))));
    }

    log.error("method: onError, context: {}", context, throwable);
  }

  private String getStackTraceAsString(Throwable throwable) {
    var stringWriter = new StringWriter();
    var printWriter = new PrintWriter(stringWriter, true);
    throwable.printStackTrace(printWriter);
    return stringWriter
        .getBuffer()
        // 2000 is the maximum length for the fields in the Slack section block.
        .substring(0, 1850)
        .replaceAll("\\R+|\"|\\\\", "#");
  }
}
