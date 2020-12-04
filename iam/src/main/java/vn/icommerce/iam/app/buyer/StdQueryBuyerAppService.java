package vn.icommerce.iam.app.buyer;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.icommerce.iam.app.component.BuyerInfoHolder;
import vn.icommerce.sharedkernel.app.component.ResultStatus;
import vn.icommerce.sharedkernel.app.component.SearchEngine;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.DomainCode;

/**
 * Standard implementation for the query account service.
 */
@Service
@Slf4j
public class StdQueryBuyerAppService implements QueryBuyerAppService {

  private static final String BUYER_INDEX = "buyer";

  private final SearchEngine searchEngine;

  private final BuyerInfoHolder buyerInfoHolder;

  /**
   * Constructor to inject dependencies.
   */
  public StdQueryBuyerAppService(
      SearchEngine searchEngine,
      BuyerInfoHolder buyerInfoHolder
  ) {
    this.searchEngine = searchEngine;
    this.buyerInfoHolder = buyerInfoHolder;
  }


  @Override
  public Map<String, Object> get(List<String> includeFields) {
    log.info("method: get , includeFields: {}", includeFields);
    var buyerId = buyerInfoHolder.getBuyerId();

    var getResult = searchEngine.get(BUYER_INDEX, buyerId.toString(), includeFields);

    if (getResult.getResultStatus() == ResultStatus.FAILED) {
      throw new DomainException(DomainCode.BUYER_NOT_FOUND, buyerId, getResult.getMessage());
    }

    log.info("method: get, result: {}", getResult.getValue());

    return getResult.getValue();
  }
}
