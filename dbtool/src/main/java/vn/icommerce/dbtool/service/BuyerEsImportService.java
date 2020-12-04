package vn.icommerce.dbtool.service;

import java.util.List;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import vn.icommerce.common.jackson.JacksonExecutor;
import vn.icommerce.common.springtx.SpringTxExecutor;
import vn.icommerce.dbtool.repository.BuyerBuyerId;
import vn.icommerce.dbtool.repository.SpringBuyerRepository;
import vn.icommerce.sharedkernel.domain.model.Buyer;

@Component
public class BuyerEsImportService
    extends AbstractEsImportService<Buyer, Long, BuyerBuyerId> {

  private final SpringBuyerRepository springBuyerRepository;

  public BuyerEsImportService(
      SpringTxExecutor springTxExecutor,
      RestHighLevelClient client,
      JacksonExecutor jacksonExecutor,
      SpringBuyerRepository springBuyerRepository
  ) {
    super(springTxExecutor, client, jacksonExecutor);
    this.springBuyerRepository = springBuyerRepository;
  }

  @Override
  protected Slice<BuyerBuyerId> findById(Pageable pageable) {
    return springBuyerRepository.findByBuyerIdNotNull(pageable);
  }

  @Override
  protected Long transform(BuyerBuyerId view) {
    return view.getBuyerId();
  }

  @Override
  protected List<Buyer> findByIdIn(List<Long> idList) {
    return springBuyerRepository.findByBuyerIdIn(idList);
  }

  @Override
  protected String getIndexName() {
    return "buyer";
  }

  @Override
  protected IndexRequest buildIndexRequest(Buyer buyer) {
    return new IndexRequest(getIndexName())
        .id(buyer.getBuyerId().toString())
        .source(jacksonExecutor.serializeAsBytes(buyer), XContentType.JSON)
        .version(buyer.getUpdatedAt().toInstant().toEpochMilli())
        .versionType(VersionType.EXTERNAL);
  }
}
