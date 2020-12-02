
package vn.icommerce.iam.app.buyer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Command object having the information to create an buyer account.
 *
 */
@Accessors(chain = true)
@ToString(exclude = "password")
@Data
public class CreateBuyerCmd {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String password;

  @NotBlank
  private String buyerName;
}
