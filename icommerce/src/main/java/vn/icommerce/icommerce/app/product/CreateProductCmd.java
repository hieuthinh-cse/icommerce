package vn.icommerce.icommerce.app.product;

import java.math.BigDecimal;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Command object that has all the information to create a product.
 */
@Accessors(chain = true)
@Data
public class CreateProductCmd {

  @NotBlank
  private String productName;

  @NotNull
  @Min(1L)
  @Max(999999999999L)
  private BigDecimal productPrice;

  @NotBlank
  private String productBrand;

  @NotBlank
  private String productColour;
}
