
package vn.icommerce.iam.app.buyer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import vn.icommerce.sharedkernel.domain.model.SocialPlatform;

/**
 * Command object having the information to create an buyer account.
 */
@Accessors(chain = true)
@Data
public class CreateBuyerCmd {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String socialId;

  @NotBlank
  private SocialPlatform socialPlatform;

  @NotBlank
  private String buyerName;
}
