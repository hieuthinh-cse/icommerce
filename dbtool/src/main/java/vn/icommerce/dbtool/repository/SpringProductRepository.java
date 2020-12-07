package vn.icommerce.dbtool.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.icommerce.sharedkernel.domain.model.Product;

/**
 * The supported repository interface for {@link Product} that is implemented automatically by
 * Spring.
 */
public interface SpringProductRepository extends JpaRepository<Product, Long> {

  Slice<ProductProductId> findByProductIdNotNull(Pageable pageable);

  List<Product> findByProductIdIn(List<Long> productIdList);
}
