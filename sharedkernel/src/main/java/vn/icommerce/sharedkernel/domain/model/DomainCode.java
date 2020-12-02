package vn.icommerce.sharedkernel.domain.model;

/**
 * This enum represents the result of an operation.
 */
public enum DomainCode {

  // 200xxx is for successful operation
  REQUEST_PROCESSED_SUCCESSFULLY(200_000, "Request processed successfully"),

  // 400xxx is for invalid input parameter
  MISSING_REQUEST_HEADER(400_000, "Missing request header"),
  METHOD_ARGUMENT_TYPE_MISMATCH(400_001, "Method argument type mismatch"),
  HTTP_MESSAGE_NOT_READABLE(400_002, "Http message not readable"),
  MISSING_SERVLET_REQUEST_PARAMETER(400_003, "Missing servlet request parameter"),
  ARGUMENT_NOT_VALID(400_004, "Method argument not valid"),

  // 401xxx is for unauthorized exception
  UNAUTHORIZED(401_000, "Unauthorized"),
  TOKEN_EXPIRED(401_001, "Token expired"),
  INVALID_TOKEN(401_002, "Invalid token"),

  // 403xxx is for forbidden exception
  FORBIDDEN(403_000, "Forbidden"),

  // 404xxx is for resource not found exception
  PRODUCT_NOT_FOUND(404_000, "Account %s not found"),

  // 405xxx is for unsupported http request method
  HTTP_REQUEST_METHOD_NOT_SUPPORTED(405_000, "Http request method not supported"),

  // 406xxx is for calling external service error
  SEARCH_REQUEST(406_013, "Failed to search with query %s. Reason: %s"),

  // 409xxx is for resource modification conflict

  // 415xxx is for unsupported media type error
  HTTP_MEDIA_TYPE_NOT_SUPPORTED(415_000, "Http media type not supported"),

  // 422xxx is for business logic error

  // 500xx is for internal error
  UNKNOWN_ERROR(500_000, "Unknown error");


  private final int value;

  private final String msgTemplate;

  DomainCode(int value, String msgTemplate) {
    this.value = value;
    this.msgTemplate = msgTemplate;
  }

  public int value() {
    return value;
  }

  public String valueAsString() {
    return String.valueOf(value);
  }

  public String getMsgTemplate() {
    return name() + "(" + value + ")" + " - " + msgTemplate;
  }
}
