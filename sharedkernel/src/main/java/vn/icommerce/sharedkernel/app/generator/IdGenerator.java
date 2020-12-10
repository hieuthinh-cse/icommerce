package vn.icommerce.sharedkernel.app.generator;

import java.security.SecureRandom;

/**
 * Id Generator help generate random id.
 *
 *
 *
 *
 */
public class IdGenerator {

  private static final SecureRandom secureRandom = new SecureRandom();

  /**
   * Generate a random number between min (inclusive) and max (exclusive).
   * @param min the lower bound (inclusive)
   * @param max the upper bound (exclusive)
   * @return random number between min (inclusive) and max (exclusive)
   */
  public static long generate(long min, long max) {
    if (min < 0) {
      throw new IllegalArgumentException("min must be greater than or equal to zero");
    }
    if (max <= min) {
      throw new IllegalArgumentException("max must be greater than min");
    }

    long num = Math.abs(secureRandom.nextLong());
    return num % (max - min) + min;
  }

}
