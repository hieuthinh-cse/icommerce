package vn.icommerce.icommerce.app.order;

import java.util.List;
import java.util.Map;
import vn.icommerce.sharedkernel.app.component.Query;
import vn.icommerce.sharedkernel.app.component.SearchResult.SearchDataResult;

public interface BuyerQueryOrderAppService {

  Map<String, Object> get(Long orderId, List<String> includeFields);

  SearchDataResult search(Query query);
}
