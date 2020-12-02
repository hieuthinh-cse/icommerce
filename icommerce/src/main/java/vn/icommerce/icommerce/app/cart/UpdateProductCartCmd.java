/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.app.cart;

import java.util.UUID;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Command object that has all the information to update a product.
 */
@Accessors(chain = true)
@Data
public class UpdateProductCartCmd {

  @NotNull
  private UUID cartItemId;

  @Min(1)
  private Integer quantity;
}
