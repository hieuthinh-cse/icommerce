package vn.icommerce.icommerce.app.address;

import vn.icommerce.sharedkernel.app.component.Query;
import vn.icommerce.sharedkernel.app.component.SearchResult.SearchDataResult;

public interface BuyerQueryAddressAppService {

  /**
   * Searches the roles matching the given query.
   *
   * @param query the query having information to search
   * @return the dto
   */
  SearchDataResult search(Query query);
}
