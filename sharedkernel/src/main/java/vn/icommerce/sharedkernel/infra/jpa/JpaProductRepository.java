package vn.icommerce.sharedkernel.infra.jpa;

import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.DomainCode;
import vn.icommerce.sharedkernel.domain.model.Product;
import vn.icommerce.sharedkernel.domain.repository.ProductRepository;


/**
 * Implementation that uses the Jpa/Spring implementation to perform business.
 */
@Slf4j
@Repository
public class JpaProductRepository implements ProductRepository {

  private final SpringProductRepository springProductRepository;

  private final EntityManager entityManager;

  public JpaProductRepository(SpringProductRepository SpringProductRepository,
      EntityManager entityManager) {
    this.springProductRepository = SpringProductRepository;
    this.entityManager = entityManager;
  }

  @Override
  public Product requireById(Long productId) {
    var product = springProductRepository.findById(productId)
        .orElseThrow(() -> new DomainException(DomainCode.PRODUCT_NOT_FOUND, productId));

    log.info("method: requireById, phone: {} , product: {}", productId, product);

    return product;
  }

  @Override
  public void create(Product profile) {
    entityManager.persist(profile);

    log.info("method: create, product: {}", profile);
  }

  @Override
  public Optional<Product> findById(Long productId) {
    var productOptional = springProductRepository.findById(productId);

    log.info("method: findById, productId: {} , productOptional: {}", productId, productOptional);

    return productOptional;
  }
}
