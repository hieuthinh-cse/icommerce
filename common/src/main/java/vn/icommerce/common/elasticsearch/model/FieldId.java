/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.elasticsearch.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Interface to the query business on the document field in Elasticsearch.
 *
 * <p>Created on 9/23/19.
 *
 * @author khoanguyenminh
 */
@Accessors(chain = true)
@Data
public class FieldId {

  private String index;

  private String fieldName;

  private FieldType fieldType;
}
