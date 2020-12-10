

package vn.icommerce.common.elasticsearch.strategy;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.util.StringUtils;
import vn.icommerce.common.elasticsearch.model.FieldState;

/**
 * Implementation for the common behavior when searching field.
 *
 *
 *
 *
 */
public abstract class AbstractSearchStrategy implements SearchStrategy {

  protected abstract QueryBuilder doGetQueryBuilder(FieldState fieldState);

  private QueryBuilder getNestedQueryBuilderIfAny(String[] fields, int index,
      FieldState fieldState) {
    if (index == fields.length - 1) {
      if (StringUtils.hasText(fieldState.getFieldValue())) {
        return doGetQueryBuilder(fieldState);
      } else {
        return QueryBuilders.existsQuery(fieldState.getFieldName());
      }
    } else {
      var path = new StringBuilder(fields[0]);
      for (int i = 0; i < index; i++) {
        path
            .append(".")
            .append(fields[i]);
      }

      return QueryBuilders.nestedQuery(
          path.toString(),
          getNestedQueryBuilderIfAny(fields, index + 1, fieldState),
          ScoreMode.None);
    }
  }

  @Override
  public QueryBuilder getQueryBuilder(FieldState fieldState) {
    var fields = fieldState
        .getFieldName()
        .split("\\.");

    return getNestedQueryBuilderIfAny(fields, 0, fieldState);
  }
}
