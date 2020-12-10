package vn.icommerce.iam.app.address;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.icommerce.iam.app.component.BuyerInfoHolder;
import vn.icommerce.sharedkernel.app.component.OutboxEngine;
import vn.icommerce.sharedkernel.app.component.TxManager;
import vn.icommerce.sharedkernel.domain.event.ShippingAddressCreatedEvent;
import vn.icommerce.sharedkernel.domain.model.Address;
import vn.icommerce.sharedkernel.domain.repository.ShippingAddressRepository;

@Service
@Slf4j
public class StdBuyerAddressAppService implements BuyerAddressAppService {

  private final ShippingAddressRepository shippingAddressRepository;

  private final BuyerInfoHolder buyerInfoHolder;

  private final TxManager txManager;

  private final OutboxEngine outboxEngine;

  public StdBuyerAddressAppService(
      ShippingAddressRepository shippingAddressRepository,
      BuyerInfoHolder buyerInfoHolder,
      TxManager txManager,
      OutboxEngine outboxEngine) {
    this.shippingAddressRepository = shippingAddressRepository;
    this.buyerInfoHolder = buyerInfoHolder;
    this.txManager = txManager;
    this.outboxEngine = outboxEngine;
  }

  @Override
  public String createAddress(CreateAddressCmd cmd) {
    log.info("method: createAddress");
    var buyerId = buyerInfoHolder.getBuyerId();

    var addressId = txManager.doInTx(() -> {

      var address = new Address()
          .setBuyerId(buyerId)
          .setName(cmd.getName())
          .setDefaultAddress(cmd.getDefaultAddress())
          .setPhoneNumber(cmd.getPhoneNumber())
          .setRegion(cmd.getRegion())
          .setStreet(cmd.getStreet());

      var defaultAddress = shippingAddressRepository.findDefaultByBuyerId(buyerId);

      if (defaultAddress.isPresent() && cmd.getDefaultAddress()) {
        address.setDefaultAddress(true);
        defaultAddress.get().setDefaultAddress(false);
      }

      if (defaultAddress.isEmpty()) {
        address.setDefaultAddress(true);
      }

      shippingAddressRepository.create(address);

      outboxEngine.create(new ShippingAddressCreatedEvent()
          .setAddressId(address.getAddressId()));

      return address.getAddressId();
    });

    log.info("method: createAddress, , addressId: {}", addressId.toString());

    return addressId.toString();
  }
}
