package vn.icommerce.iam.app.address;

import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.icommerce.iam.app.component.BuyerInfoHolder;
import vn.icommerce.sharedkernel.app.component.Query;
import vn.icommerce.sharedkernel.app.component.ResultStatus;
import vn.icommerce.sharedkernel.app.component.SearchEngine;
import vn.icommerce.sharedkernel.app.component.SearchResult;
import vn.icommerce.sharedkernel.app.component.SearchResult.SearchDataResult;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.DomainCode;

@Service
@Slf4j
public class StdBuyerQueryAddressAppService implements BuyerQueryAddressAppService {

  private static final String BUYER_ADDRESS_INDEX = "address";

  private final SearchEngine searchEngine;

  private final BuyerInfoHolder buyerInfoHolder;


  public StdBuyerQueryAddressAppService(
      SearchEngine searchEngine,
      BuyerInfoHolder buyerInfoHolder) {
    this.searchEngine = searchEngine;
    this.buyerInfoHolder = buyerInfoHolder;
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

    SearchResult searchResult = searchEngine.search(BUYER_ADDRESS_INDEX, query);

    if (searchResult.getResultStatus() != ResultStatus.OK) {
      throw new DomainException(DomainCode.SEARCH_REQUEST, query.toString(),
          searchResult.getMessage());
    }
    log.info("method: search, searchDataResult: {}", searchResult.getSearchDataResult());
    return searchResult.getSearchDataResult();
  }
}
