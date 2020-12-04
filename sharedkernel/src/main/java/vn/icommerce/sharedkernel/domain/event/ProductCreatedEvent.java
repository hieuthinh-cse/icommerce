/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.sharedkernel.domain.event;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This event occurs when a product is created.
 *
 */
@Accessors(chain = true)
@Data
public class ProductCreatedEvent implements DomainEvent {

  private Long productId;

}
