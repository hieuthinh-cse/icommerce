package vn.icommerce.iam.app.component;

import vn.icommerce.sharedkernel.domain.model.BuyerToken;

/**
 * Interface to the token cryptography engine.
 */
public interface TokenCryptoEngine {

  /**
   * Signs the given token.
   *
   * @param token the token to sign
   * @return the signed token.
   */
  String signBuyerToken(BuyerToken token);

  /**
   * Verifies the encoded account token.
   *
   * @param encodedToken encoded token
   * @return account token
   */
  BuyerToken verifyBuyerToken(String encodedToken);
}
