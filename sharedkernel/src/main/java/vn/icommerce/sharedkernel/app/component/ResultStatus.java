/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.sharedkernel.app.component;

/**
 * Represents the result when calling the external services.
 *
 * <p>Created on 9/4/19.
 *
 * @author khoanguyenminh
 */
public enum ResultStatus {

  /**
   * Indicates that the connection was established & the related business was performed
   * successfully.
   */
  OK,

  /**
   * Indicates that the connection was established & the related business was performed with failed
   * result because some required conditions were not satisfied.
   */
  FAILED
}
