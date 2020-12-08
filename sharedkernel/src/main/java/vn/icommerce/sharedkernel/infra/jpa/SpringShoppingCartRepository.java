package vn.icommerce.sharedkernel.infra.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.icommerce.sharedkernel.domain.model.ShoppingCart;
import vn.icommerce.sharedkernel.domain.model.ShoppingCartStatus;

/**
 * The supported repository interface for {@link ShoppingCart} that is implemented automatically by
 * Spring.
 */
public interface SpringShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

  Optional<ShoppingCart> findByBuyerIdAndStatus(Long buyerID, ShoppingCartStatus status);
}
