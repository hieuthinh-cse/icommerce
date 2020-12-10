

package vn.icommerce.common.elasticsearch.strategy;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import vn.icommerce.common.elasticsearch.model.FieldState;

/**
 * Implementation for searching on the {@code text} field.
 *
 *
 *
 *
 */
public class QueryStrategy extends AbstractSearchStrategy {

  @Override
  protected QueryBuilder doGetQueryBuilder(FieldState fieldState) {
    var textFieldName = String.format("%s.text", fieldState.getFieldName());

    return QueryBuilders
        .boolQuery()
        .should(QueryBuilders.matchQuery(textFieldName, fieldState.getFieldValue()))
        .should(QueryBuilders.matchQuery(textFieldName, fieldState.getFieldValue())
            .fuzziness(Fuzziness.TWO))
        .should(QueryBuilders.matchPhrasePrefixQuery(textFieldName, fieldState.getFieldValue()));
  }
}
