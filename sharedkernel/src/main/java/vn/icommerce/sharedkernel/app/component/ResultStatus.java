

package vn.icommerce.sharedkernel.app.component;

/**
 * Represents the result when calling the external services.
 *
 *
 *
 *
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
