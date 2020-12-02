package vn.icommerce.iam.app.buyertoken;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.icommerce.iam.app.component.Encoder;
import vn.icommerce.iam.app.component.TokenCryptoEngine;
import vn.icommerce.sharedkernel.app.component.OutboxEngine;
import vn.icommerce.sharedkernel.app.component.TxManager;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.Buyer;
import vn.icommerce.sharedkernel.domain.model.BuyerToken;
import vn.icommerce.sharedkernel.domain.model.DomainCode;
import vn.icommerce.sharedkernel.domain.repository.BuyerRepository;

/**
 * Standard implementation for the operator service.
 */
@Service
@Slf4j
public class StdBuyerTokenAppService implements BuyerTokenAppService {

  private final BuyerRepository buyerRepository;

  private final TxManager txManager;

  private final Encoder encoder;

  private final TokenCryptoEngine tokenCryptoEngine;

  private final long tokenExpirationInS;

  /**
   * Constructor to inject dependencies.
   */
  StdBuyerTokenAppService(
      @Value("#{tokenConfig.expirationInS}") long tokenExpirationInS,
      BuyerRepository buyerRepository,
      TokenCryptoEngine tokenCryptoEngine,
      TxManager txManager,
      Encoder encoder,
      OutboxEngine outboxEngine
  ) {
    this.encoder = encoder;
    this.tokenExpirationInS = tokenExpirationInS;
    this.buyerRepository = buyerRepository;
    this.tokenCryptoEngine = tokenCryptoEngine;
    this.txManager = txManager;
  }

  private Buyer getAccountInfo(String email, String password) {
    var buyer = buyerRepository
        .findByEmail(email)
//        .filter(Buyer::active)
        .orElseThrow(() -> new DomainException(DomainCode.INVALID_CREDENTIALS, email));

    if (!encoder.matches(password, buyer.getPassword())) {
      throw new DomainException(DomainCode.INVALID_CREDENTIALS, email);
    }


    return buyer;
  }

  @Override
  public String create(CreateBuyerTokenCmd cmd) {
    log.info("method: create, email: {}", cmd.getEmail());

    String token = txManager.doInTx(() -> {
      var buyer = getAccountInfo(cmd.getEmail(), cmd.getPassword());

      var buyerToken = new BuyerToken()
          .setBuyerId(buyer.getBuyerId())
          .setExpirationDuration(tokenExpirationInS);

      return tokenCryptoEngine.signBuyerToken(buyerToken);
    });

    log.info("method: create");

    return token;
  }
}
