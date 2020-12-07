package vn.icommerce.sharedkernel.domain.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Accessors(chain = true)
@Getter
@Setter
@ToString
public class SearchHistory {

  private Long buyerId;

  private List<String> query;

  private List<String> filter;
}
