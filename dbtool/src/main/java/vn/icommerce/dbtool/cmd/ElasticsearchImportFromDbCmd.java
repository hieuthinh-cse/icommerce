
package vn.icommerce.dbtool.cmd;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.icommerce.dbtool.service.EsImportService;

@Slf4j
@Component
public class ElasticsearchImportFromDbCmd extends AbstractElasticsearchCmd {

  private final Map<String, EsImportService> esIndexServiceFactory = new HashMap<>();

  public ElasticsearchImportFromDbCmd() {
    super(null);
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
