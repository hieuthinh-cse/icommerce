package vn.icommerce.icommerce.app.component;

/**
 * Represents the result code when calling otp service.
 *
 * <p>Created on 3/16/20.
 *
 * @author hungphamchanh
 */

public enum OtpResultCode {

  /**
   * Indicates that the otp has expired.
   */
  EXPIRED_OTP,

  /**
   * Indicates that the otp has exceed retry attempt.
   */
  EXCEED_ATTEMPT,

  /**
   * Indicates that user has enter wrong otp.
   */
  OTP_WRONG,

  /**
   * Indicates that otp success.
   */
  OTP_SUCCESS,

  /**
   * Indicates that phone number is invalid.
   */
  OTP_PHONE_WRONG,

  OTHER_ERROR
}
