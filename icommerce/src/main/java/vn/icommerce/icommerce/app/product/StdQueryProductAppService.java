package vn.icommerce.icommerce.app.product;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.icommerce.sharedkernel.app.component.Query;
import vn.icommerce.sharedkernel.app.component.ResultStatus;
import vn.icommerce.sharedkernel.app.component.SearchEngine;
import vn.icommerce.sharedkernel.app.component.SearchResult;
import vn.icommerce.sharedkernel.app.component.SearchResult.SearchDataResult;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.DomainCode;

@Service
@Slf4j
public class StdQueryProductAppService implements QueryProductAppService {

  private static final String PRODUCT_INDEX = "product";

  private final SearchEngine searchEngine;


  public StdQueryProductAppService(
      SearchEngine searchEngine
  ) {
    this.searchEngine = searchEngine;
  }


  @Override
  public Map<String, Object> get(Long productId, List<String> includeFields) {
    log.info("method: get, productId: {} , includeFields: {}", productId, includeFields);
    var getResult = searchEngine.get(PRODUCT_INDEX, productId.toString(), includeFields);

    if (getResult.getResultStatus() != ResultStatus.OK) {
      throw new DomainException(DomainCode.PRODUCT_NOT_FOUND, productId, getResult.getMessage());
    }

    log.info("method: get, value: {}", getResult.getValue());

    return getResult.getValue();
  }

  @Override
  public SearchDataResult search(Query query) {
    log.info("method: search, query: {}", query);

    SearchResult searchResult = searchEngine.search(PRODUCT_INDEX, query);

    if (searchResult.getResultStatus() != ResultStatus.OK) {
      throw new DomainException(DomainCode.SEARCH_REQUEST, query.toString(),
          searchResult.getMessage());
    }
    log.info("method: search, searchDataResult: {}", searchResult.getSearchDataResult());
    return searchResult.getSearchDataResult();
  }
}
