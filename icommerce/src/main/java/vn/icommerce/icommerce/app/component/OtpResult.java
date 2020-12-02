package vn.icommerce.icommerce.app.component;

import lombok.Data;
import lombok.experimental.Accessors;
import vn.icommerce.sharedkernel.app.component.ResultStatus;

/**
 * This object represents the result of triggering OTP to wallet.
 *
 * <p>Created on 3/16/20.
 *
 * @author hungphamchanh
 */
@Data
@Accessors(chain = true)
public class OtpResult {

  private ResultStatus resultStatus;

  private String message;

  private OtpResultCode otpResultCode;

  public boolean isFailed() {
    return ResultStatus.FAILED == resultStatus;
  }
}
