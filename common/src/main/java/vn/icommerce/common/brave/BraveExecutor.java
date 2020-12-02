package vn.icommerce.common.brave;

import brave.Tracer;
import brave.Tracer.SpanInScope;
import java.math.BigInteger;
import java.util.Objects;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class BraveExecutor {

  public static final String TRACE_ID = "traceId";

  public static final String SPAN_ID = "spanId";

  public static final String CORRELATION_ID = "correlation_id";

  private final Tracer tracer;

  public BraveExecutor(Tracer tracer) {
    this.tracer = tracer;
  }

  public void run(String traceId, String spanId, String correlationId, Runnable runnable) {
    if (StringUtils.isEmpty(traceId)
        || StringUtils.isEmpty(spanId)) {
      runnable.run();
    } else {
      var currentSpan = tracer.currentSpan();
      if (Objects.isNull(currentSpan)) {
        currentSpan = tracer.newTrace();
      }
      var context = currentSpan.context();
      var traceIdInLong = new BigInteger(traceId, 16).longValue();
      var spanIdInLong = new BigInteger(spanId, 16).longValue();
      var newContext = context
          .toBuilder()
          .traceId(traceIdInLong)
          .spanId(spanIdInLong)
          .build();
      var newChild = tracer.newChild(newContext);
      try (var ws = tracer.withSpanInScope(newChild.start())) {
        MDC.put(CORRELATION_ID, correlationId);
        runnable.run();
        MDC.remove(CORRELATION_ID);
      } finally {
        newChild.finish();
      }
    }
  }
}
