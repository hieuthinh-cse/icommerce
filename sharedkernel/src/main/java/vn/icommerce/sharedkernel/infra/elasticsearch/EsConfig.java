

package vn.icommerce.sharedkernel.infra.elasticsearch;

import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import vn.icommerce.common.elasticsearch.model.FieldId;
import vn.icommerce.common.elasticsearch.model.FieldType;
import vn.icommerce.common.elasticsearch.processor.SearchProcessor;
import vn.icommerce.common.elasticsearch.strategy.RangeFilterStrategy;

/**
 * Configuration for Elasticsearch REST high level client.
 *
 */
@Configuration
public class EsConfig {

  private final SearchProcessor searchProcessor;

  public EsConfig(SearchProcessor searchProcessor) {
    this.searchProcessor = searchProcessor;
  }

  @PostConstruct
  public void registerSearchField() {
    searchProcessor
        .registerField(
            new FieldId()
                .setIndex("product")
                .setFieldName("createdAt")
                .setFieldType(FieldType.KEYWORD),
            RangeFilterStrategy.class)
        .registerField(
            new FieldId()
                .setIndex("product")
                .setFieldName("updatedAt")
                .setFieldType(FieldType.KEYWORD),
            RangeFilterStrategy.class);
  }
}
