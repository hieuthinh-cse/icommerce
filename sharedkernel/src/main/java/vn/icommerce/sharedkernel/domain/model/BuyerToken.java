

package vn.icommerce.sharedkernel.domain.model;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * This entity represents a token used to access API.
 *
 */
@Accessors(chain = true)
@Setter
@Getter
@ToString
public class BuyerToken {

  private Long buyerId;

  private String sessionId;

  private OffsetDateTime expiredAt;

  public BuyerToken setExpirationDuration(long expirationInSecond) {
    expiredAt = OffsetDateTime
        .now()
        .plusSeconds(expirationInSecond);

    return this;
  }
}
