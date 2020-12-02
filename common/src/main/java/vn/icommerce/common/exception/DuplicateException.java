/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.exception;


/**
 * Thrown when there is a duplicated event.
 *
 * <p>Created on 10/4/19.
 *
 * @author vanlh
 */
public class DuplicateException extends RuntimeException {

  public DuplicateException(String msg) {
    super(msg);
  }
}
