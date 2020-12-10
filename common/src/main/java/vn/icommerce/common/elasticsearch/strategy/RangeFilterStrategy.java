

package vn.icommerce.common.elasticsearch.strategy;

import static java.util.stream.Collectors.toList;

import java.util.stream.Stream;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.util.NumberUtils;
import vn.icommerce.common.elasticsearch.model.FieldState;

/**
 * Implementation for searching within a provided {@code numeric} or {@code date} range.
 *
 *
 *
 *
 */
public class RangeFilterStrategy extends AbstractSearchStrategy {

  @Override
  protected QueryBuilder doGetQueryBuilder(FieldState fieldState) {
    var numberList = Stream
        .of(fieldState.getFieldValue().split("\\s+"))
        .map(strValue -> NumberUtils.parseNumber(strValue, Long.class))
        .collect(toList());

    return QueryBuilders
        .rangeQuery(fieldState.getFieldName())
        .gte(numberList.get(0))
        .lte(numberList.get(1));
  }
}
