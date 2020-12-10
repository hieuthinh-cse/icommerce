package vn.icommerce.icommerce.app.order;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.icommerce.icommerce.app.component.BuyerInfoHolder;
import vn.icommerce.sharedkernel.app.component.Query;
import vn.icommerce.sharedkernel.app.component.ResultStatus;
import vn.icommerce.sharedkernel.app.component.SearchEngine;
import vn.icommerce.sharedkernel.app.component.SearchResult;
import vn.icommerce.sharedkernel.app.component.SearchResult.SearchDataResult;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.DomainCode;

@Service
@Slf4j
public class StdBuyerQueryOrderAppService implements BuyerQueryOrderAppService {

  private static final String SHOPPING_CART_INDEX = "order";

  private final SearchEngine searchEngine;

  private final BuyerInfoHolder buyerInfoHolder;


  public StdBuyerQueryOrderAppService(
      SearchEngine searchEngine,
      BuyerInfoHolder buyerInfoHolder) {
    this.searchEngine = searchEngine;
    this.buyerInfoHolder = buyerInfoHolder;
  }


  @Override
  public Map<String, Object> get(Long orderId, List<String> includeFields) {
    log.info("method: get, orderId: {} , includeFields: {}", orderId, includeFields);

    var buyerId = buyerInfoHolder.getBuyerId();

    var getResult = searchEngine.get(SHOPPING_CART_INDEX, orderId.toString(), includeFields);

    if (getResult.getResultStatus() != ResultStatus.OK) {
      throw new DomainException(DomainCode.ORDER_NOT_FOUND, orderId, getResult.getMessage());
    }

    if (getResult.getValue().getOrDefault("buyerId", 0).equals(buyerId)) {
      throw new DomainException(DomainCode.ORDER_NOT_FOUND, orderId, getResult.getMessage());
    }

    log.info("method: get, value: {}", getResult.getValue());

    return getResult.getValue();
  }

  @Override
  public SearchDataResult search(Query query) {
    log.info("method: search, query: {}", query);

    var buyerId = buyerInfoHolder.getBuyerId();

    var filter = Collections.singletonList(String.format("buyerId:%s", buyerId));

    if (query.getFilter().isEmpty()) {
      query.setFilter(filter);
    } else {
      query.getFilter().addAll(filter);
    }

    SearchResult searchResult = searchEngine.search(SHOPPING_CART_INDEX, query);

    if (searchResult.getResultStatus() != ResultStatus.OK) {
      throw new DomainException(DomainCode.SEARCH_REQUEST, query.toString(),
          searchResult.getMessage());
    }
    log.info("method: search, searchDataResult: {}", searchResult.getSearchDataResult());
    return searchResult.getSearchDataResult();
  }
}
