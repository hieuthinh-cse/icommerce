

package vn.icommerce.common.elasticsearch.strategy;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import vn.icommerce.common.elasticsearch.model.FieldState;

/**
 * Implementation for filtering on the {@code keyword} field.
 *
 *
 *
 *
 */
public class FilterStrategy extends AbstractSearchStrategy {

  @Override
  protected QueryBuilder doGetQueryBuilder(FieldState fieldState) {
    var valueList = Stream
        .of(fieldState.getFieldValue().split("\\s+"))
        .collect(Collectors.toList());

    return QueryBuilders.termsQuery(fieldState.getFieldName(), valueList);
  }
}
