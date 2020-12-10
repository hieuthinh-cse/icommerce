//package vn.icommerce.essync.app;
//
//import java.util.UUID;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import vn.icommerce.sharedkernel.app.component.SearchEngine;
//import vn.icommerce.sharedkernel.domain.repository.ShippingAddressRepository;
//
///**
// * Standard implementation for the account service.
// */
//@Slf4j
//@Component
//public class StdShippingAddress implements ShippingAddressConsumer {
//
//  private static final String BUYER_ADDRESS_INDEX = "address";
//
//  private final ShippingAddressRepository shippingAddressRepository;
//
//  private final SearchEngine searchEngine;
//
//  public StdShippingAddress(
//      ShippingAddressRepository shippingAddressRepository,
//      SearchEngine searchEngine) {
//    this.shippingAddressRepository = shippingAddressRepository;
//    this.searchEngine = searchEngine;
//  }
//
//  @Transactional(readOnly = true)
//  public void indexById(UUID addressId) {
//    shippingAddressRepository
//        .findById(addressId)
//        .ifPresent(address -> searchEngine.index(
//            BUYER_ADDRESS_INDEX,
//            address.getAddressId().toString(),
//            address.getUpdatedAt().toInstant().toEpochMilli(),
//            address));
//
//    log.info("method: indexById, addressId: {}", addressId);
//  }
//}
