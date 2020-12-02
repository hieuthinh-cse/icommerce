package vn.icommerce.sharedkernel.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import vn.icommerce.sharedkernel.app.generator.IdGenerator;


@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = "productId")
@ToString
@Entity
@Table(name = "product")
public class Product {

  @Id
  @Column(name = "product_id", unique = true, nullable = false)
  @Setter(AccessLevel.NONE)
  private Long productId = Long.sum(700_000_000L, IdGenerator.generate(0L, 99_999_999L));

//  name, price, brand, colour

  @Column(name = "product_name", nullable = false)
  private String productName;

  @Column(name = "product_price", nullable = false)
  private BigDecimal productPrice = new BigDecimal(0);

  @Setter(AccessLevel.NONE)
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  @Column(name = "created_by", nullable = false, updatable = false)
  @CreatedBy
  private String createdBy;

  @Column(name = "updated_by", nullable = false)
  @LastModifiedBy
  private String updatedBy;
}
