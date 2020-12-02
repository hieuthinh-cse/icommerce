/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.sharedkernel.app.component;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This class represents an abstract result when invoking an unit of logic processing.
 *
 * <p>Created on 9/4/19.
 *
 * @author khoanguyenminh
 */
@ToString
@EqualsAndHashCode
public abstract class AbstractResult {

  protected ResultStatus status;

  protected String message;

  public AbstractResult(ResultStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  public boolean isFailed() {
    return ResultStatus.FAILED == status;
  }

  public String getMessage() {
    return message;
  }
}
