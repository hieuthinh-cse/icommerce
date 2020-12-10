package vn.icommerce.sharedkernel.infra.jpa;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.icommerce.sharedkernel.domain.model.Product;

/**
 * The supported repository interface for {@link Product} that is implemented automatically by
 * Spring.
 */
public interface SpringProductRepository extends JpaRepository<Product, Long> {

  List<Product> findByProductIdIn(List<Long> productIds);
}
