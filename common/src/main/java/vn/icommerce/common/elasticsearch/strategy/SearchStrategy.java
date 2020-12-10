

package vn.icommerce.common.elasticsearch.strategy;

import org.elasticsearch.index.query.QueryBuilder;
import vn.icommerce.common.elasticsearch.model.FieldState;

/**
 * Interface to the query business on the document field in Elasticsearch.
 *
 *
 *
 *
 */
public interface SearchStrategy {

  /**
   * Gets the query builder for the given field state.
   *
   * @param fieldState the field state to get the query builder
   */
  QueryBuilder getQueryBuilder(FieldState fieldState);
}
