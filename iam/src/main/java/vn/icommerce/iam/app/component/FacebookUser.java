package vn.icommerce.iam.app.component;

import lombok.Data;
import lombok.experimental.Accessors;
import vn.icommerce.sharedkernel.app.component.ResultStatus;

@Accessors(chain = true)
@Data
public class FacebookUser {

  private ResultStatus resultStatus;

  private String id;

  private String fullName;

  private String email;
}