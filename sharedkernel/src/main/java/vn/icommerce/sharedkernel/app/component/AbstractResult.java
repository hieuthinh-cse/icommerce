

package vn.icommerce.sharedkernel.app.component;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This class represents an abstract result when invoking an unit of logic processing.
 *
 *
 *
 *
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
