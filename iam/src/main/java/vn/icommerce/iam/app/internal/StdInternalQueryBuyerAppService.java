package vn.icommerce.iam.app.internal;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.icommerce.sharedkernel.app.component.Query;
import vn.icommerce.sharedkernel.app.component.ResultStatus;
import vn.icommerce.sharedkernel.app.component.SearchEngine;
import vn.icommerce.sharedkernel.app.component.SearchResult.SearchDataResult;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.DomainCode;

/**
 * Standard implementation for the query account service.
 */
@Service
@Slf4j
public class StdInternalQueryBuyerAppService implements InternalQueryBuyerAppService {

  private static final String BUYER_INDEX = "buyer";

  private final SearchEngine searchEngine;

  /**
   * Constructor to inject dependencies.
   */
  public StdInternalQueryBuyerAppService(SearchEngine searchEngine) {
    this.searchEngine = searchEngine;
  }

  @Override
  public SearchDataResult search(Query query) {
    log.info("method: search, query: {}", query);

    var searchResult = searchEngine.search(BUYER_INDEX, query);

    if (searchResult.getResultStatus() == ResultStatus.FAILED) {
      throw new DomainException(DomainCode.SEARCH_REQUEST, query.toString(),
          searchResult.getMessage());
    }

    log.info("method: search, searchDataResult: {}", searchResult.getSearchDataResult());

    return searchResult.getSearchDataResult();
  }

  @Override
  public Map<String, Object> get(Long buyerId, List<String> includeFields) {
    log.info("method: get, buyerId: {} , includeFields: {}", buyerId, includeFields);

    var getResult = searchEngine.get(BUYER_INDEX, buyerId.toString(), includeFields);

    if (getResult.getResultStatus() == ResultStatus.FAILED) {
      throw new DomainException(DomainCode.BUYER_NOT_FOUND, buyerId, getResult.getMessage());
    }

    log.info("method: get, result: {}", getResult.getValue());

    return getResult.getValue();
  }
}
