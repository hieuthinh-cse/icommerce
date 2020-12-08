package vn.icommerce.sharedkernel.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = "cartItemId")
@Entity
@Table(name = "shopping_cart_item")
public class ShoppingCartItem {

  @Id
  @Column(name = "shopping_cart_item_id", unique = true, nullable = false)
  private UUID cartItemId = UUID.randomUUID();

  @JsonIgnore
  @ManyToOne(targetEntity = ShoppingCart.class)
  @JoinColumn(name = "shopping_cart_id", nullable = false)
  private ShoppingCart shoppingCart;

  @Column(name = "quantity")
  private Integer quantity = 1;

  @Column(name = "price")
  private BigDecimal price = BigDecimal.ZERO;

  @Column(name = "product_id", nullable = false)
  private Long productId;


  @JsonIgnore
  @Transient
  private Product product;

  public BigDecimal getSubTotal() {
    return price.multiply(BigDecimal.valueOf(quantity));
  }
}
