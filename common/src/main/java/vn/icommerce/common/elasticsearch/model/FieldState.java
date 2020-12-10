

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
public class FieldState {

  private FieldId fieldId;

  private boolean positive;

  private String fieldValue;

  public boolean isText() {
    return fieldId.getFieldType() == FieldType.TEXT;
  }

  public String getFieldName() {
    return fieldId.getFieldName();
  }
}
