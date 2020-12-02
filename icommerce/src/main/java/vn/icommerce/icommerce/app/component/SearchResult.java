package vn.icommerce.icommerce.app.component;

import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import vn.icommerce.sharedkernel.app.component.ResultStatus;

/**
 * This object represents the result of a search.
 *
 */
@Accessors(chain = true)
@Data
public class SearchResult {

  private ResultStatus resultStatus;

  private String message;

  private SearchDataResult searchDataResult;

  @Accessors(chain = true)
  @Data
  @ToString(exclude = "records")
  public static class SearchDataResult {

    private List<Map<String, Object>> records;

    private int page;

    private int pageSize;

    private Long total;

    private int limitTotal = 10_000;
  }
}
