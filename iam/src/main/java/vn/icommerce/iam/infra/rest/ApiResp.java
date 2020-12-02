package vn.icommerce.iam.infra.rest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Represents the response object returning to client.
 *
 */
@Data
@Accessors(chain = true)
public class ApiResp {

  private int code;

  private String message;

  private Object data;

}