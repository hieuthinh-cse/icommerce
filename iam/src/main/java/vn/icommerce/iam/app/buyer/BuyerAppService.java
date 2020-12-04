package vn.icommerce.iam.app.buyer;

/**
 * Interface to the service that handles the business related to the account use cases.
 *
 */
public interface BuyerAppService {

  /**
   * Creates a new account given the command.
   *
   * @param cmd the command having info to create an account
   * @return the id of the new account
   */
  Long create(CreateBuyerCmd cmd);
}
