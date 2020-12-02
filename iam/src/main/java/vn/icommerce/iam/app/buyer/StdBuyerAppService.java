package vn.icommerce.iam.app.buyer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.icommerce.iam.app.component.Encoder;
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
 * <p>Created on 8/20/19.
 *
 * @author khoanguyenminh
 */
@Service
@Slf4j
public class StdBuyerAppService implements BuyerAppService {

  private final BuyerRepository buyerRepository;

  private final Encoder encoder;

  private final TxManager txManager;

  private final OutboxEngine outboxEngine;

  /**
   * Constructor to inject dependencies.
   *
   * @param buyerRepository ...
   * @param encoder         ...
   * @param txManager       ...
   * @param outboxEngine    ...
   */
  StdBuyerAppService(
      BuyerRepository buyerRepository,
      Encoder encoder,
      TxManager txManager,
      OutboxEngine outboxEngine
  ) {
    this.buyerRepository = buyerRepository;
    this.encoder = encoder;
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

    var encodedPassword = encoder.encode(cmd.getPassword());

    var lowerCasedEmail = cmd.getEmail().toLowerCase();

    var buyer = new Buyer()
        .setEmail(lowerCasedEmail)
        .setPassword(encodedPassword)
        .setAccountName(cmd.getBuyerName())
        .setStatus(BuyerStatus.ACTIVE);

    txManager.doInTx(() -> {
      requireNonExistingEmail(lowerCasedEmail);

      buyerRepository.create(buyer);

      outboxEngine.create(
          new BuyerCreatedEvent()
              .setBuyerId(buyer.getBuyerId()));
    });

    log.info("method: create, buyerId: {}", buyer.getBuyerId());

    return buyer.getBuyerId();
  }
}
