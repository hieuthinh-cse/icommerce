/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.app.cart;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Command object that has all the information to update a product.
 */
@Accessors(chain = true)
@Data
public class DeleteProductCartCmd {

  @NotNull
  private UUID productId;
}
