package vn.icommerce.sharedkernel.domain.repository;

import java.util.Optional;
import java.util.UUID;
import vn.icommerce.sharedkernel.domain.model.Address;

/**
 * This repository manages the persistence logic of {@link Address} entity.
 */
public interface ShippingAddressRepository {

  Address requireDefaultByBuyerId(Long buyerId);

  Optional<Address> findDefaultByBuyerId(Long buyerId);

  void create(Address address);

  Optional<Address> findById(UUID addressId);
}
