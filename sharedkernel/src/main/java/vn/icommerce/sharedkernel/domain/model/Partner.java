package vn.icommerce.sharedkernel.domain.model;

import java.util.UUID;

/**
 * Predefined partners for transaction.
 *
 */
public enum Partner {
  ICOMMERCE("cc9c2560-fad1-470c-9f30-f4eb2611e671"),
  IAM("f0a5dedb-1aa9-08f8-0463-9310b2a0b0bb");

  private final UUID partnerId;

  Partner(String partnerId) {
    this.partnerId = UUID.fromString(partnerId);
  }

  public UUID getPartnerId() {
    return this.partnerId;
  }

  public static String getNameByValue(String value) {
    for (Partner partner : Partner.values()) {
      if (partner.getPartnerId().toString().equals(value)) {
        return partner.name();
      }
    }
    return value;
  }
}
