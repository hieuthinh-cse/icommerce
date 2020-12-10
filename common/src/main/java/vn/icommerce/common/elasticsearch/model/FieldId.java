

package vn.icommerce.common.elasticsearch.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Interface to the query business on the document field in Elasticsearch.
 *
 *
 *
 *
 */
@Accessors(chain = true)
@Data
public class FieldId {

  private String index;

  private String fieldName;

  private FieldType fieldType;
}
