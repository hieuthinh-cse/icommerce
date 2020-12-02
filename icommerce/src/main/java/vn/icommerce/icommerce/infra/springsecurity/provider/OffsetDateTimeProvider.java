package vn.icommerce.icommerce.infra.springsecurity.provider;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;


/**
 * Implementation that uses the current {@link OffsetDateTime#now()} to perform the business logic.
 *
 * <p>Created on 11/24/19.
 *
 * @author khoanguyenminh
 */
@Component
public class OffsetDateTimeProvider implements DateTimeProvider {
  @Override
  public Optional<TemporalAccessor> getNow() {
    return Optional.of(OffsetDateTime.now());
  }
}
