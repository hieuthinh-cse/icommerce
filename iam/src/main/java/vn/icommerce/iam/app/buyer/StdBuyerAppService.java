package vn.icommerce.iam.app.buyer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.icommerce.iam.app.buyertoken.BuyerTokenAppService;
import vn.icommerce.iam.app.component.Encoder;
import vn.icommerce.iam.app.component.SocialGateway;
import vn.icommerce.sharedkernel.app.component.OutboxEngine;
import vn.icommerce.sharedkernel.app.component.TxManager;
import vn.icommerce.sharedkernel.domain.event.BuyerCreatedEvent;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.Buyer;
import vn.icommerce.sharedkernel.domain.model.BuyerStatus;
import vn.icommerce.sharedkernel.domain.model.DomainCode;
import vn.icommerce.sharedkernel.domain.repository.BuyerRepository;

/**
 * Standard implementation for the account service.
 *
 *
 *
 *
 */
@Service
@Slf4j
public class StdBuyerAppService implements BuyerAppService {

  private final BuyerRepository buyerRepository;

  private final TxManager txManager;

  private final OutboxEngine outboxEngine;

  /**
   * Constructor to inject dependencies.
   *
   * @param buyerRepository      ...
   * @param txManager            ...
   * @param outboxEngine         ...
   */
  StdBuyerAppService(
      BuyerRepository buyerRepository,
      TxManager txManager,
      OutboxEngine outboxEngine
  ) {
    this.buyerRepository = buyerRepository;
    this.txManager = txManager;
    this.outboxEngine = outboxEngine;
  }

  private void requireNonExistingEmail(String email) {
    if (buyerRepository.existsByEmail(email)) {
      throw new DomainException(DomainCode.BUYER_EXISTING, email);
    }
  }

  @Override
  public Long create(CreateBuyerCmd cmd) {
    log.info("method: create, cmd: {}", cmd);

    var lowerCasedEmail = cmd.getEmail().toLowerCase();

    var buyerId = txManager.doInTx(() -> {
      requireNonExistingEmail(lowerCasedEmail);

      var buyer = new Buyer()
          .setEmail(lowerCasedEmail)
          .setSocialId(cmd.getSocialId())
          .setPlatform(cmd.getSocialPlatform())
          .setAccountName(cmd.getBuyerName())
          .setStatus(BuyerStatus.ACTIVE);

      buyerRepository.create(buyer);
      outboxEngine.create(
          new BuyerCreatedEvent()
              .setBuyerId(buyer.getBuyerId()));

      buyerRepository.create(buyer);

      outboxEngine.create(
          new BuyerCreatedEvent()
              .setBuyerId(buyer.getBuyerId()));

      return buyer.getBuyerId();
    });

    log.info("method: create, buyerId: {}", buyerId);

    return buyerId;
  }


}
