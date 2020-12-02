/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.sharedkernel.domain.event;

import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This event occurs when a product is updated.
 */
@Accessors(chain = true)
@Data
public class ProductUpdatedEvent implements DomainEvent {

  private Long productId;

  private BigDecimal oldPrice;

  private BigDecimal newPrice;
}
