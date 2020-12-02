/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.infra.rest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Represents the response object returning to client.
 *
 * <p>Created on 8/24/19.
 *
 * @author khoanguyenminh
 */
@Data
@Accessors(chain = true)
public class ApiResp {

  private int code;

  private String message;

  private Object data;

}