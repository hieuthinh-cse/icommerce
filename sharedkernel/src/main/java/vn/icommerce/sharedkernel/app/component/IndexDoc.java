package vn.icommerce.sharedkernel.app.component;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class IndexDoc {

  private String indexName;

  private String indexDocId;

  private long version;

  private Object data;
}
