package vn.icommerce.icommerce.app.product;

import java.util.List;
import java.util.Map;
import vn.icommerce.sharedkernel.app.component.Query;
import vn.icommerce.sharedkernel.app.component.SearchResult.SearchDataResult;

public interface QueryProductAppService {

  /**
   * Gets the wallet given the id.
   *
   * @param productId      the id to get wallet
   * @param includeFields the fields to return in the result
   * @return the dto
   */
  Map<String, Object> get(Long productId, List<String> includeFields);

  /**
   * Searches the roles matching the given query.
   *
   * @param query the query having information to search
   * @return the dto
   */
  SearchDataResult search(Query query);
}
