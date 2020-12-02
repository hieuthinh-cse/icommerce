/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.sharedkernel.app.component;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Represents a document to index.
 *
 * <p>Created on 8/20/19.
 *
 * @author khoanguyenminh
 */
@Accessors(chain = true)
@Data
public class IndexDoc {

  private String indexName;

  private String indexDocId;

  private long version;

  private Object data;
}
