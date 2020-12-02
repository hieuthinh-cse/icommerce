/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.app.component;

/**
 * Interface to the password generator.
 *
 * <p>Created on 8/24/19.
 *
 * @author khoanguyenminh
 */
public interface PasswordGenerator {

  /**
   * Generates a random password.
   *
   * @return the generated password.
   */
  String execute();
}
