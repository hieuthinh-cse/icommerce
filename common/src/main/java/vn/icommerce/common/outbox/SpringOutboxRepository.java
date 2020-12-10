

package vn.icommerce.common.outbox;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The supported repository interface for {@link Outbox} that is implemented automatically by
 * Spring.
 *
 *
 *
 *
 */
public interface SpringOutboxRepository extends JpaRepository<Outbox, UUID> {

  List<Outbox> findTop1000ByOrderByCreatedAt();
}
