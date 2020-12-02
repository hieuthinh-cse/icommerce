package vn.icommerce.icommerce.app.component;

/**
 * Interface to the service that handles the otp.
 *
 * <p>Created on 3/17/20.
 *
 * @author hungphamchanh
 */
public interface OtpGateway {

  /**
   * Triggers an OTP to be sent to phone.
   *
   * @param phone           the client phone number to send the OTP to
   * @param verifyRequestId requestId
   * @param msgTemplate     the message template to send to client phone
   * @return the result
   */
  OtpResult triggerOtpPhone(String phone, String verifyRequestId, String msgTemplate);


  /**
   * Verify an OTP.
   *
   * @param otp             the OTP
   * @param verifyRequestId requestId
   * @return the result
   */
  OtpResult verifyOtpPhone(String otp, String verifyRequestId);
}