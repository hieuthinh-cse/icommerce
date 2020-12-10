package vn.icommerce.sharedkernel.domain.model;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
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
 * <p>Shopping cart is responsible for storing and carrying
 * shopping cart information.Shopping Cart consists of {@link ShoppingCartItem} which represents
 * individual lines items associated with the shopping cart</p>
 */
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = "orderId")
@Entity
@NamedEntityGraph(
    name = "orderAggregate",
    attributeNodes = {
        @NamedAttributeNode("cart"),
    }
)
@Table(name = "shopping_order")
public class Order {

  @Id
  @Column(name = "order_id", unique = true, nullable = false)
  @Setter(AccessLevel.NONE)
  private Long orderId = Long.sum(700_000_000L, IdGenerator.generate(0L, 99_999_999L));

  @Column(name = "buyer_id")
  private Long buyerId;

  @OneToOne(optional = false)
  @JoinColumn(
      name = "cart_id",
      referencedColumnName = "shopping_cart_id",
      updatable = false,
      nullable = false
  )
  private ShoppingCart cart;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private OrderStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method", nullable = false)
  private PaymentMethod paymentMethod;

  @Embedded
  private ShippingAddress shippingAddress;

  @Setter(AccessLevel.NONE)
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;
}
