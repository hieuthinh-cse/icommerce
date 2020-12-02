/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

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
public class AccountToken {

  private Long accountId;

  private String sessionId;

  private OffsetDateTime expiredAt;

  public AccountToken setExpirationDuration(long expirationInSecond) {
    expiredAt = OffsetDateTime
        .now()
        .plusSeconds(expirationInSecond);

    return this;
  }
}
