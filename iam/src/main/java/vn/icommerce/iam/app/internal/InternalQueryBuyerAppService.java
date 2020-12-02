package vn.icommerce.iam.app.internal;

import java.util.List;
import java.util.Map;
import vn.icommerce.sharedkernel.app.component.Query;
import vn.icommerce.sharedkernel.app.component.SearchResult.SearchDataResult;

/**
 * Interface to the service that perform the query logic for account business.
 */
public interface InternalQueryBuyerAppService {

  SearchDataResult search(Query query);

  /**
   * Gets the account given the id.
   *
   * @param includeFields the fields to return in the result
   * @return the dto
   */
  Map<String, Object> get(Long buyerId, List<String> includeFields);
}
