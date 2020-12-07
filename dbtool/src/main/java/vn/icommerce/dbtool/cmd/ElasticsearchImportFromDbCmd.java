
package vn.icommerce.dbtool.cmd;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.icommerce.dbtool.service.BuyerEsImportService;
import vn.icommerce.dbtool.service.EsImportService;
import vn.icommerce.dbtool.service.ProductEsImportService;
import vn.icommerce.dbtool.service.ShoppingCartEsImportService;

@Slf4j
@Component
public class ElasticsearchImportFromDbCmd extends AbstractElasticsearchCmd {

  private final Map<String, EsImportService> esIndexServiceFactory = new HashMap<>();

  public ElasticsearchImportFromDbCmd(
      BuyerEsImportService buyerEsImportService,
      ShoppingCartEsImportService shoppingCartEsImportService,
      ProductEsImportService productEsImportService
  ) {
    super(null);

    esIndexServiceFactory.put("buyer", buyerEsImportService);
    esIndexServiceFactory.put("shopping_cart", shoppingCartEsImportService);
    esIndexServiceFactory.put("product", productEsImportService);
  }

  @Override
  public void run() {
    if (optionSet.contains("*")) {
      esIndexServiceFactory
          .values()
          .forEach(EsImportService::importFromDb);
    } else {
      optionSet
          .stream()
          .map(esIndexServiceFactory::get)
          .filter(Objects::nonNull)
          .forEach(EsImportService::importFromDb);
    }
  }
}
