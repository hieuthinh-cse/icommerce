package vn.icommerce.sharedkernel.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.icommerce.sharedkernel.domain.model.Order;

/**
 * The supported repository interface for {@link Order} that is implemented automatically by
 * Spring.
 */
public interface SpringOrderRepository extends JpaRepository<Order, Long> {

}
