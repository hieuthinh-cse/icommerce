package vn.icommerce.sharedkernel.infra.jpa;

import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.DomainCode;
import vn.icommerce.sharedkernel.domain.model.Address;
import vn.icommerce.sharedkernel.domain.repository.ShippingAddressRepository;

/**
 * Implementation that uses the Jpa/Spring implementation to perform business.
 */
@Slf4j
@Repository
public class JpaShippingAddressRepository implements ShippingAddressRepository {

  private final SpringShippingAddressRepository springShippingAddressRepository;

  private final EntityManager entityManager;

  public JpaShippingAddressRepository(
      SpringShippingAddressRepository springShippingAddressRepository,
      EntityManager entityManager) {
    this.springShippingAddressRepository = springShippingAddressRepository;
    this.entityManager = entityManager;
  }


  @Override
  public void create(Address address) {
    entityManager.persist(address);

    log.info("method: create, address: {}", address);
  }

  @Override
  public Optional<Address> findById(UUID addressId) {
    var addressOptional = springShippingAddressRepository.findById(addressId);

    log.info("method: findById, addressId: {} , addressOptional: {}", addressId, addressOptional);

    return addressOptional;
  }

  @Override
  public Address requireDefaultByBuyerId(Long buyerId) {
    var address = springShippingAddressRepository.findByBuyerIdAndDefaultAddress(buyerId, true)
        .orElseThrow(
            () -> new DomainException(DomainCode.BUYER_DEFAULT_ADDRESS_NOT_FOUND, buyerId));

    log.info("method: requireDefaultByBuyerId, buyerId: {}, address: {}", buyerId, address);

    return address;
  }

  @Override
  public Optional<Address> findDefaultByBuyerId(Long buyerId) {
    var optionalAddress = springShippingAddressRepository
        .findByBuyerIdAndDefaultAddress(buyerId, true);

    log.info("method: requireDefaultByBuyerId, buyerId: {}, optionalAddress: {}", buyerId,
        optionalAddress);

    return optionalAddress;
  }
}
