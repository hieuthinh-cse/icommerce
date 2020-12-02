package vn.icommerce.iam.app.buyer;

import java.util.List;
import java.util.Map;

/**
 * Interface to the service that perform the query logic for account business.
 */
public interface QueryBuyerAppService {

  /**
   * Gets the account given the id.
   *
   * @param includeFields the fields to return in the result
   * @return the dto
   */
  Map<String, Object> get(List<String> includeFields);
}
