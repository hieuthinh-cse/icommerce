/*
 * Copyright 2020 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.springretry;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Created on 5/12/20.
 *
 * @author khoanguyenminh
 */
@Accessors(chain = true)
@Data
public class RetryNotificationThresholdReachedEvent {

  private int retryCount;

  private int retryNotificationThreshold;

  private String stackTrace;
}
