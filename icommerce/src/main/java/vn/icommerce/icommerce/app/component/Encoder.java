/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.app.component;

/**
 * Interface to password encoder.
 *
 * <p>Created on 8/24/19.
 *
 * @author khoanguyenminh
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
