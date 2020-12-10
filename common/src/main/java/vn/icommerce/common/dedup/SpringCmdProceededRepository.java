

package vn.icommerce.common.dedup;


import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The supported repository interface for {@link CmdProceeded} that is implemented automatically by
 * Spring.
 *
 *
 *
 *
 */
public interface SpringCmdProceededRepository extends JpaRepository<CmdProceeded, String> {

  List<CmdProceeded> findTop1000ByCreatedAtBeforeOrderByCreatedAt(OffsetDateTime date);
}
