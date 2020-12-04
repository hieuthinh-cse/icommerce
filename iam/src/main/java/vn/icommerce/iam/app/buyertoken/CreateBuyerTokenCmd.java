package vn.icommerce.iam.app.buyertoken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Command object having the information to login to an account.
 *
 */
@Accessors(chain = true)
@Data
@ToString(exclude = "password")
public class CreateBuyerTokenCmd {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String password;
}
