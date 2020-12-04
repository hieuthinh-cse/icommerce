package vn.icommerce.iam.app.component;

/**
 * Interface to password encoder.
 *
 */
public interface Encoder {

  /**
   * Encodes the raw password.
   *
   * @param rawPassword the raw password to encode
   * @return the encoded password.
   */
  String encode(String rawPassword);

  /**
   * Verifies the encoded password obtained from the storage matches the submitted raw password
   * after it too is encoded.
   *
   * @param rawPassword     the raw password to encode and match
   * @param encodedPassword the encoded password from storage to compare with
   * @return true if the raw password, after encoding, matches the encoded password from storage
   */
  boolean matches(String rawPassword, String encodedPassword);
}
