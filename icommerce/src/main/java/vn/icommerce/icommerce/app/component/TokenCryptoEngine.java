/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.app.component;

import vn.icommerce.sharedkernel.domain.model.BuyerToken;

/**
 * Interface to the token cryptography engine.
 *
 * <p>Created on 8/24/19.
 *
 * @author khoanguyenminh
 */
public interface TokenCryptoEngine {

  /**
   * Signs the given accountToken.
   *
   * @param buyerToken the accountToken to sign
   * @return the signed accountToken.
   */
  String signAccountToken(BuyerToken buyerToken);

  /**
   * Verifies the encoded account token.
   *
   * @param encodedToken encoded token
   * @return account token
   */
  BuyerToken verifyAccountToken(String encodedToken);
}
