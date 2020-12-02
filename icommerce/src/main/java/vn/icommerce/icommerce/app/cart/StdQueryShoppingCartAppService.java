package vn.icommerce.icommerce.app.cart;

import java.util.Arrays;
import java.util.Map;
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
public class StdQueryShoppingCartAppService implements QueryShoppingCartAppService {

  private static final String SHOPPING_CART_INDEX = "shopping_cart";

  private final SearchEngine searchEngine;

  private final BuyerInfoHolder buyerInfoHolder;


  public StdQueryShoppingCartAppService(
      SearchEngine searchEngine,
      BuyerInfoHolder buyerInfoHolder) {
    this.searchEngine = searchEngine;
    this.buyerInfoHolder = buyerInfoHolder;
  }


  @Override
  public Map<String, Object> get(Query query) {
    log.info("method: get , query: {}", query);

    var buyerId = buyerInfoHolder.getBuyerId();

    var filter = Arrays
        .asList(String.format("buyerId:%s", buyerId), String.format("status:%s", "PROCESSING"));

    if (query.getFilter().isEmpty()) {
      query.setFilter(filter);
    } else {
      query.getFilter().addAll(filter);
    }

    var searchResult = searchEngine.search(SHOPPING_CART_INDEX, query);

    if (searchResult.getResultStatus() != ResultStatus.OK) {
      throw new DomainException(DomainCode.SEARCH_REQUEST, query.toString(),
          searchResult.getMessage());
    }

    var getResult = searchResult.getSearchDataResult().getRecords().get(0);

    log.info("method: get, value: {}", getResult);

    return getResult;
  }

  @Override
  public SearchDataResult search(Query query) {
    log.info("method: search, query: {}", query);

    SearchResult searchResult = searchEngine.search(SHOPPING_CART_INDEX, query);

    if (searchResult.getResultStatus() != ResultStatus.OK) {
      throw new DomainException(DomainCode.SEARCH_REQUEST, query.toString(),
          searchResult.getMessage());
    }
    log.info("method: search, searchDataResult: {}", searchResult.getSearchDataResult());
    return searchResult.getSearchDataResult();
  }
}
