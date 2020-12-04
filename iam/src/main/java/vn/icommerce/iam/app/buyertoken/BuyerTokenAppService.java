package vn.icommerce.iam.app.buyertoken;


/**
 * Interface to the service that handles the business related to the token use cases.
 *
 */
public interface BuyerTokenAppService {

  /**
   * Creates a token given the command.
   *
   * @param cmd the command having info to create a token
   * @return the token if successfully
   */
  String create(CreateBuyerTokenCmd cmd);
}
