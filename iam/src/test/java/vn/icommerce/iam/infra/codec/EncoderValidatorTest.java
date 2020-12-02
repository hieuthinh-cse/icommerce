package vn.icommerce.iam.infra.codec;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EncoderValidatorTest {

  private CodecConfig codecConfig = new CodecConfig();

  private CodecEncoder encoder = new CodecEncoder(codecConfig);

  public EncoderValidatorTest() throws NoSuchAlgorithmException {
  }

  @Test
  public void givenPasswordAndSalt_WhenEncodePassword_ThenReturnMatch()
      throws InvalidKeySpecException, NoSuchAlgorithmException {
    var password = "123456";
    var salt = "ef9f8d2f761bf6cd3a73307e0e98005dd4a21f0eb6931efe515c1f5186955205";
//    var match = encoder.pbkdf2(password, salt, 10000, 128);

    System.out.println(encoder.hashPassword(password, salt, 10000, 128));
    System.out.println(encoder.generateSalt());
  }
}
