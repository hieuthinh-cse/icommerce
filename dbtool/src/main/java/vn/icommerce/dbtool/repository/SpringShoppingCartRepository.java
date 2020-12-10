

package vn.icommerce.dbtool.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.icommerce.sharedkernel.domain.model.Buyer;
import vn.icommerce.sharedkernel.domain.model.ShoppingCart;

/**
 * The supported repository interface for {@link Buyer} that is implemented automatically by
 * Spring.
 *
 *
 *
 *
 */
public interface SpringShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

  Slice<ShoppingCartShoppingCartId> findByShoppingCartIdNotNull(Pageable pageable);

  List<ShoppingCart> findByShoppingCartIdIn(List<Long> shoppingCartIdList);
}
