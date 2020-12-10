package vn.icommerce.sharedkernel.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;
import vn.icommerce.sharedkernel.app.generator.IdGenerator;

/**
 *
 * shopping cart information.Shopping Cart consists of {@link ShoppingCartItem}
 * which represents individual lines items associated with the shopping cart</p>
 *
 */
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = "shoppingCartId")
@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {

  @Id
  @Column(name = "shopping_cart_id", unique = true, nullable = false)
  @Setter(AccessLevel.NONE)
  private Long shoppingCartId = Long.sum(800_000_000L, IdGenerator.generate(0L, 99_999_999L));

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "shoppingCart")
  @JsonInclude(Include.NON_EMPTY)
  private Set<ShoppingCartItem> items = new HashSet<>();

  @Column(name = "buyer_id")
  private Long buyerId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private ShoppingCartStatus status;

  @Setter(AccessLevel.NONE)
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  @OneToOne(cascade = CascadeType.ALL,
      mappedBy = "cart",
      orphanRemoval = true)
  @JsonIgnore
  private Order order;

  public BigDecimal getSubTotal() {
    return items
        .stream()
        .map(ShoppingCartItem::getSubTotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
