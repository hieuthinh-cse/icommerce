/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.app.product;

import java.math.BigDecimal;
import java.util.UUID;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Command object that has all the information to update a product.
 */
@Accessors(chain = true)
@Data
public class UpdateProductCmd {

  private Long productId;

  @NotBlank
  private String productName;

  @NotNull
  @Min(0L)
  @Max(999999999999L)
  private BigDecimal productPrice;

}
