

package vn.icommerce.sharedkernel.domain.exception;

import vn.icommerce.sharedkernel.domain.model.DomainCode;

/**
 * Thrown when there is a domain-specific exception happens.
 *
 *
 *
 *
 */
public class DomainException extends RuntimeException {

  private final DomainCode code;

  private final Object[] args;

  /**
   * Constructs the exception.
   *
   * @param code the result code of the operation
   */
  public DomainException(DomainCode code, Object... args) {
    super(String.format(code.getMsgTemplate(), args));
    this.code = code;
    this.args = args;
  }

  /**
   * Returns the error code of this exception.
   *
   * @return the error code
   */
  public DomainCode getCode() {
    return code;
  }

  /**
   * Returns the array of arguments that will be filled in for params within the exception message.
   *
   * @return the array of arguments
   */
  public Object[] getArgs() {
    return args;
  }
}
