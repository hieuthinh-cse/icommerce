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
  BUYER_NOT_FOUND(404_000, "Buyer %s not found"),
  PRODUCT_NOT_FOUND(404_001, "Product %s not found"),
  PROCESSING_CART_NOT_FOUND(404_002, "Cart buyer %s not found"),
  BUYER_DEFAULT_ADDRESS_NOT_FOUND(404_004, "Buyer %s's default address not found"),
  ORDER_NOT_FOUND(404_005, "Order %s not found"),
  INVALID_CREDENTIALS(404_007, "Credentials %s with its password is invalid"),

  // 405xxx is for unsupported http request method
  HTTP_REQUEST_METHOD_NOT_SUPPORTED(405_000, "Http request method not supported"),

  // 406xxx is for calling external service error
  SOCIAL_LOGIN_FAILED(406_001, "Failed to unable to login Facebook"),
  SEARCH_REQUEST(406_013, "Failed to search with query %s. Reason: %s"),
  EXTERNAL_SVC_RESPONSE_ERROR(406_028, "Error when calling %s. Reason: %s. Error data: %s"),
  EXTERNAL_SVC_CONNECTION_ERROR(406_029, "Error when connecting to %s. Reason: %s"),
  EXTERNAL_SVC_INTERRUPTION_ERROR(406_030, "Error when processing %s. Reason: %s"),
  EXTERNAL_SVC_EMPTY_RESPONSE_ERROR(406_031, "Received empty response body from %s"),


  // 409xxx is for resource modification conflict
  BUYER_EXISTING(409_000, "Account %s already exists"),
  PROCESSING_CART_EXISTING(409_001, "Processing cart %s already exists"),

  // 415xxx is for unsupported media type error
  HTTP_MEDIA_TYPE_NOT_SUPPORTED(415_000, "Http media type not supported"),

  // 422xxx is for business logic error
  SHOPPING_CART_EMPTY(422_000, "Shopping cart is empty"),
  SHOPPING_CART_CHANGED(422_009, "Shopping cart changed."),

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
