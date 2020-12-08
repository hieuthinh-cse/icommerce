package vn.icommerce.sharedkernel.infra.jpa;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.icommerce.sharedkernel.domain.model.Address;

/**
 * The supported repository interface for {@link Address} that is implemented automatically
 * by Spring.
 */
public interface SpringShippingAddressRepository extends JpaRepository<Address, UUID> {

  Optional<Address> findByBuyerIdAndDefaultAddress(Long buyerId, Boolean defaultAddress);
}
