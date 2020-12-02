package vn.icommerce.iam.infra.argon2;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.icommerce.iam.app.component.Encoder;

/**
 * Implementation that uses the Argon2 strong hashing function to perform the logic.
 *
 */
@Component
public class Argon2MerchantEncoder implements Encoder {

  private final Argon2PasswordEncoder argon2PasswordEncoder;

  public Argon2MerchantEncoder(Argon2PasswordEncoder argon2PasswordEncoder) {
    this.argon2PasswordEncoder = argon2PasswordEncoder;
  }

  @Override
  public String encode(String rawPassword) {
    return argon2PasswordEncoder.encode(rawPassword);
  }

  @Override
  public boolean matches(String rawPassword, String encodedPassword) {
    return argon2PasswordEncoder.matches(rawPassword, encodedPassword);
  }
}
