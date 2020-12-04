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
import vn.icommerce.dbtool.repository.ShoppingCartShoppingCartId;
import vn.icommerce.dbtool.repository.SpringShoppingCartRepository;
import vn.icommerce.sharedkernel.domain.model.ShoppingCart;

@Component
public class ShoppingCartEsImportService
    extends AbstractEsImportService<ShoppingCart, Long, ShoppingCartShoppingCartId> {

  private final SpringShoppingCartRepository springShoppingCartRepository;

  public ShoppingCartEsImportService(
      SpringTxExecutor springTxExecutor,
      RestHighLevelClient client,
      JacksonExecutor jacksonExecutor,
      SpringShoppingCartRepository springShoppingCartRepository
  ) {
    super(springTxExecutor, client, jacksonExecutor);
    this.springShoppingCartRepository = springShoppingCartRepository;
  }

  @Override
  protected Slice<ShoppingCartShoppingCartId> findById(Pageable pageable) {
    return springShoppingCartRepository.findByShoppingCartIdNotNull(pageable);
  }

  @Override
  protected Long transform(ShoppingCartShoppingCartId view) {
    return view.getShoppingCartId();
  }

  @Override
  protected List<ShoppingCart> findByIdIn(List<Long> idList) {
    return springShoppingCartRepository.findByShoppingCartIdIn(idList);
  }

  @Override
  protected String getIndexName() {
    return "shopping_cart";
  }

  @Override
  protected IndexRequest buildIndexRequest(ShoppingCart cart) {
    return new IndexRequest(getIndexName())
        .id(cart.getBuyerId().toString())
        .source(jacksonExecutor.serializeAsBytes(cart), XContentType.JSON)
        .version(cart.getUpdatedAt().toInstant().toEpochMilli())
        .versionType(VersionType.EXTERNAL);
  }
}
