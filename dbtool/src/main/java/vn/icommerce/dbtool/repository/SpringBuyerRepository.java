package vn.icommerce.dbtool.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.icommerce.sharedkernel.domain.model.Buyer;

/**
 * The supported repository interface for {@link Buyer} that is implemented automatically by
 * Spring.
 */
public interface SpringBuyerRepository extends JpaRepository<Buyer, Long> {

  Slice<BuyerBuyerId> findByBuyerIdNotNull(Pageable pageable);

  List<Buyer> findByBuyerIdIn(List<Long> buyerIdList);
}
