///*
// *
// *
// *
// */
//
//package vn.icommerce.essync.app;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import vn.icommerce.sharedkernel.app.component.SearchEngine;
//import vn.icommerce.sharedkernel.domain.repository.BuyerRepository;
//
///**
// * Standard implementation for the account service.
// */
//@Slf4j
//@Component
//public class StdBuyerConsumer implements BuyerConsumer {
//
//  private static final String PRODUCT_INDEX = "buyer";
//
//  private final BuyerRepository buyerRepository;
//
//  private final SearchEngine searchEngine;
//
//  /**
//   * Constructor to inject dependencies.
//   */
//  public StdBuyerConsumer(
//      BuyerRepository buyerRepository,
//      SearchEngine searchEngine) {
//    this.buyerRepository = buyerRepository;
//    this.searchEngine = searchEngine;
//  }
//
//  /**
//   * Indexes the product with the given id.
//   *
//   * @param buyerId the buyer id to index
//   */
//  @Transactional(readOnly = true)
//  public void indexById(Long buyerId) {
//    buyerRepository
//        .findById(buyerId)
//        .ifPresent(product -> searchEngine.index(
//            PRODUCT_INDEX,
//            product.getBuyerId().toString(),
//            product.getUpdatedAt().toInstant().toEpochMilli(),
//            product));
//
//    log.info("method: indexById, buyerId: {}", buyerId);
//  }
//}
