/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.app.component;

import vn.icommerce.sharedkernel.domain.model.AccountToken;
import vn.icommerce.sharedkernel.domain.model.OptToken;

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
   * @param accountToken the accountToken to sign
   * @return the signed accountToken.
   */
  String signAccountToken(AccountToken accountToken);

  /**
   * Verifies the encoded account token.
   *
   * @param encodedToken encoded token
   * @return account token
   */
  AccountToken verifyAccountToken(String encodedToken);

  /**
   * Signs the given operator token.
   *
   * @param optToken the token to sign
   * @return the signed token.
   */
  String signOptToken(OptToken optToken);

  /**
   * Verifies the encoded operator token.
   *
   * @param encodedToken encoded token
   * @return operator token
   */
  OptToken verifyOptToken(String encodedToken);
}
