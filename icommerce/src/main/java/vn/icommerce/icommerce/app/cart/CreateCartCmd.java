/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.app.cart;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Command object that has all the information to create a product.
 */
@Accessors(chain = true)
@Data
public class CreateCartCmd {

  private Long buyerId;
}
