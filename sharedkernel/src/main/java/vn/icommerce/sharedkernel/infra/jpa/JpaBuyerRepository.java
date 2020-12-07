package vn.icommerce.sharedkernel.infra.jpa;

import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.Buyer;
import vn.icommerce.sharedkernel.domain.model.DomainCode;
import vn.icommerce.sharedkernel.domain.model.SocialPlatform;
import vn.icommerce.sharedkernel.domain.repository.BuyerRepository;

/**
 * Implementation that uses the Jpa/Spring implementation to perform business.
 */
@Slf4j
@Repository
public class JpaBuyerRepository implements BuyerRepository {

  private final SpringAccountRepository springAccountRepository;

  private final EntityManager entityManager;

  public JpaBuyerRepository(SpringAccountRepository SpringAccountRepository,
      EntityManager entityManager) {
    this.springAccountRepository = SpringAccountRepository;
    this.entityManager = entityManager;
  }

  @Override
  public Buyer requireById(Long buyerId) {
    var buyer = springAccountRepository.findById(buyerId)
        .orElseThrow(() -> new DomainException(DomainCode.BUYER_NOT_FOUND, buyerId));

    log.info("method: requireById, buyerId: {} , buyer: {}", buyerId, buyer);

    return buyer;
  }

  @Override
  public Optional<Buyer> findByEmail(String email) {
    var buyerOptional = springAccountRepository.findByEmail(email);

    log.info("method: findByEmail, email: {} , buyerOptional: {}", email, buyerOptional);

    return buyerOptional;
  }

  @Override
  public boolean existsByEmail(String email) {
    var existed = springAccountRepository.existsByEmail(email);

    log.info("method: existsByEmail, email: {} , existed: {}", email, existed);

    return existed;
  }


  @Override
  public void create(Buyer buyer) {
    entityManager.persist(buyer);

    log.info("method: create, buyer: {}", buyer);
  }

  @Override
  public Optional<Buyer> findById(Long buyerId) {
    var buyerOptional = springAccountRepository.findById(buyerId);

    log.info("method: findById, buyerId: {} , buyerOptional: {}", buyerId, buyerOptional);

    return buyerOptional;
  }

  @Override
  public Optional<Buyer> findBySocialId(String socialId) {
    var buyerOptional = springAccountRepository
        .findBySocialIdAndPlatform(socialId, SocialPlatform.FACEBOOK);

    log.info("method: findBySocialId, socialId: {} , buyerOptional: {}", socialId, buyerOptional);

    return buyerOptional;
  }
}
