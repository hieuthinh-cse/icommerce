package vn.icommerce.sharedkernel.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Accessors(chain = true)
@Getter
@Setter
@ToString
public class ProductHistory {

  private Long productId;

  private BigDecimal productPrice = new BigDecimal(0);

  private OffsetDateTime updatedAt;
}
