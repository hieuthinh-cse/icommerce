/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.elasticsearch.strategy;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import vn.icommerce.common.elasticsearch.model.FieldState;

/**
 * Implementation for filtering on the {@code keyword} field.
 *
 * <p>Created on 9/23/19.
 *
 * @author khoanguyenminh
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
