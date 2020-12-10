

package vn.icommerce.sharedkernel.infra.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.icommerce.sharedkernel.domain.model.Buyer;
import vn.icommerce.sharedkernel.domain.model.SocialPlatform;

/**
 * The supported repository interface for {@link Buyer} that is implemented automatically by
 * Spring.
 */
public interface SpringAccountRepository extends JpaRepository<Buyer, Long> {

  Optional<Buyer> findByEmail(String email);

  Optional<Buyer> findBySocialIdAndPlatform(String socialId, SocialPlatform platform);

  boolean existsByEmail(String email);
}
