package vn.icommerce.iam.app.component;

import lombok.Data;
import lombok.experimental.Accessors;
import vn.icommerce.sharedkernel.app.component.ResultStatus;

@Accessors(chain = true)
@Data
public class FacebookDebug {

  private ResultStatus resultStatus;

  private Long appId;

  private Boolean isValid;
}