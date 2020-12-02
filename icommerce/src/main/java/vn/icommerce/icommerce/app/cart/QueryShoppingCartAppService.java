package vn.icommerce.icommerce.app.cart;

import java.util.Map;
import vn.icommerce.sharedkernel.app.component.Query;
import vn.icommerce.sharedkernel.app.component.SearchResult.SearchDataResult;

public interface QueryShoppingCartAppService {

  /**
   * Gets the wallet given the id.
   *
   * @return the dto
   */
  Map<String, Object> get(Query query);

  /**
   * Searches the roles matching the given query.
   *
   * @param query the query having information to search
   * @return the dto
   */
  SearchDataResult search(Query query);
}
