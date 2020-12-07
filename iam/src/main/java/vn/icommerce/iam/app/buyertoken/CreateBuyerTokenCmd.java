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
@ToString(exclude = "fbAccessToken")
public class CreateBuyerTokenCmd {

  @NotBlank
  private String fbAccessToken;
}
