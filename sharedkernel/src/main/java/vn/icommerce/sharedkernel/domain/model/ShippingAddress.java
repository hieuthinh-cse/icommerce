package vn.icommerce.sharedkernel.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Embeddable
@Accessors(chain = true)
@Getter
@Setter
public class ShippingAddress {
  @Column(name = "shipping_name", nullable = false)
  private String name;

  @Column(name = "shipping_phone_number", nullable = false)
  private String phoneNumber;

  @Column(name = "shipping_region", nullable = false)
  private String region;

  @Column(name = "shipping_street", nullable = false)
  private String street;
}
