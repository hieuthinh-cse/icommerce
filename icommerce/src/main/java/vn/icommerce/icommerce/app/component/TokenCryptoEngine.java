package vn.icommerce.icommerce.app.component;

import vn.icommerce.sharedkernel.domain.model.BuyerToken;

/**
 * Interface to the token cryptography engine.
 *
 */
public interface TokenCryptoEngine {


  /**
   * Verifies the encoded account token.
   *
   * @param encodedToken encoded token
   * @return account token
   */
  BuyerToken verifyAccountToken(String encodedToken);
}
