/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.sharedkernel.domain.event;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This event occurs when a cart is updated.
 */
@Accessors(chain = true)
@Data
public class ShoppingCartUpdatedEvent implements DomainEvent {

  private Long cartId;
}
