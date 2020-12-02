/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.elasticsearch.strategy;

import org.elasticsearch.index.query.QueryBuilder;
import vn.icommerce.common.elasticsearch.model.FieldState;

/**
 * Interface to the query business on the document field in Elasticsearch.
 *
 * <p>Created on 9/23/19.
 *
 * @author khoanguyenminh
 */
public interface SearchStrategy {

  /**
   * Gets the query builder for the given field state.
   *
   * @param fieldState the field state to get the query builder
   */
  QueryBuilder getQueryBuilder(FieldState fieldState);
}
