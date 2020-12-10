package vn.icommerce.sharedkernel.app.component;

import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents the result of linking token.
 *
 *
 *
 *
 */
@Accessors(chain = true)
@Data
public class GetResult {

  private ResultStatus resultStatus;

  private String message;

  Map<String, Object> value;
}
