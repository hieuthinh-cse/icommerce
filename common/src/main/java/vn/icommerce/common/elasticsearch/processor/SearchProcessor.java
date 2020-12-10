

package vn.icommerce.common.elasticsearch.processor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;
import vn.icommerce.common.elasticsearch.model.FieldId;
import vn.icommerce.common.elasticsearch.model.FieldState;
import vn.icommerce.common.elasticsearch.strategy.FilterStrategy;
import vn.icommerce.common.elasticsearch.strategy.QueryStrategy;
import vn.icommerce.common.elasticsearch.strategy.RangeFilterStrategy;
import vn.icommerce.common.elasticsearch.strategy.SearchStrategy;

/**
 * Factory to get the concrete fields of a query in Elasticsearch.
 *
 *
 *
 *
 */
@Component
public class SearchProcessor {

  private final Map<FieldId, SearchStrategy> registeredFieldIdMap = new HashMap<>();

  private final Map<Class<? extends SearchStrategy>, SearchStrategy> searchFieldMap = new HashMap<>();

  /**
   * Constructs the factory.
   */
  public SearchProcessor() {
    searchFieldMap.put(QueryStrategy.class, new QueryStrategy());
    searchFieldMap.put(FilterStrategy.class, new FilterStrategy());
    searchFieldMap.put(RangeFilterStrategy.class, new RangeFilterStrategy());
  }

  public SearchProcessor registerField(FieldId fieldId,
      Class<? extends SearchStrategy> clazz) {
    Optional
        .ofNullable(searchFieldMap.get(clazz))
        .ifPresentOrElse(
            searchStrategy -> registeredFieldIdMap.put(fieldId, searchStrategy),
            () -> {
              throw new RuntimeException(
                  String.format("Search field class %s is not supported!", clazz));
            });

    return this;
  }

  public SearchProcessor registerStrategy(SearchStrategy searchStrategy) {
    searchFieldMap.put(searchStrategy.getClass(), searchStrategy);

    return this;
  }

  public QueryBuilder execute(Collection<FieldState> fieldStates) {
    var boolQueryBuilder = QueryBuilders.boolQuery();

    fieldStates.forEach(fieldState -> {
      var searchField = Optional
          .ofNullable(registeredFieldIdMap.get(fieldState.getFieldId()))
          .orElse(fieldState.isText() ?
              searchFieldMap.get(QueryStrategy.class)
              : searchFieldMap.get(FilterStrategy.class));

      var queryBuilder = searchField.getQueryBuilder(fieldState);

      if (fieldState.isPositive()) {
        if (fieldState.isText()) {
          boolQueryBuilder.must(queryBuilder);
        } else {
          boolQueryBuilder.filter(queryBuilder);
        }
      } else {
        boolQueryBuilder.mustNot(queryBuilder);
      }
    });

    return boolQueryBuilder;
  }
}
