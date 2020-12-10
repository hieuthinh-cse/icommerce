package vn.icommerce.sharedkernel.app.component;

import java.util.Collections;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@QueryConstraint
public class Query {

  @Size(max = 20)
  private List<String> query = Collections.emptyList();

  @Size(max = 20)
  private List<String> filter = Collections.emptyList();

  @Size(max = 10)
  private List<String> sort = Collections.emptyList();

  @Min(0)
  private int page = 0;

  @Min(5)
  @Max(200)
  private int pageSize = 10;

  @Size(max = 20)
  private List<String> includeFields = Collections.emptyList();

  @Size(max = 10)
  private List<Object> after = Collections.emptyList();
}
