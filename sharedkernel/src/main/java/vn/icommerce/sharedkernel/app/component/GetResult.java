package vn.icommerce.sharedkernel.app.component;

import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents the result of linking token.
 *
 * <p>Created on 9/5/19.
 *
 * @author vutc
 */
@Accessors(chain = true)
@Data
public class GetResult {

  private ResultStatus resultStatus;

  private String message;

  Map<String, Object> value;
}
