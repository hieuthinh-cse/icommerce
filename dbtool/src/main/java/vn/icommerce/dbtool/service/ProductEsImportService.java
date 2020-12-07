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
import vn.icommerce.dbtool.repository.ProductProductId;
import vn.icommerce.dbtool.repository.SpringProductRepository;
import vn.icommerce.sharedkernel.domain.model.Product;

@Component
public class ProductEsImportService
    extends AbstractEsImportService<Product, Long, ProductProductId> {

  private final SpringProductRepository springProductRepository;

  public ProductEsImportService(
      SpringTxExecutor springTxExecutor,
      RestHighLevelClient client,
      JacksonExecutor jacksonExecutor,
      SpringProductRepository springProductRepository
  ) {
    super(springTxExecutor, client, jacksonExecutor);
    this.springProductRepository = springProductRepository;
  }

  @Override
  protected Slice<ProductProductId> findById(Pageable pageable) {
    return springProductRepository.findByProductIdNotNull(pageable);
  }

  @Override
  protected Long transform(ProductProductId view) {
    return view.getProductId();
  }

  @Override
  protected List<Product> findByIdIn(List<Long> idList) {
    return springProductRepository.findByProductIdIn(idList);
  }

  @Override
  protected String getIndexName() {
    return "product";
  }

  @Override
  protected IndexRequest buildIndexRequest(Product Product) {
    return new IndexRequest(getIndexName())
        .id(Product.getProductId().toString())
        .source(jacksonExecutor.serializeAsBytes(Product), XContentType.JSON)
        .version(Product.getUpdatedAt().toInstant().toEpochMilli())
        .versionType(VersionType.EXTERNAL);
  }
}
