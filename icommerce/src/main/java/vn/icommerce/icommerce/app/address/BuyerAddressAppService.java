package vn.icommerce.icommerce.app.address;

/**
 * Interface to the service that handles the use case of product business logic.
 */
public interface BuyerAddressAppService {

  String createAddress(CreateAddressCmd cmd);
}
