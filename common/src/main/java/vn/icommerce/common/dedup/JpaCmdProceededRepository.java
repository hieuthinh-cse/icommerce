

package vn.icommerce.common.dedup;


import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import vn.icommerce.common.exception.DuplicateException;

/**
 * Implementation that uses the Jpa/Spring implementation to perform outbox business.
 *
 *
 *
 *
 */
@Repository
@Slf4j
public class JpaCmdProceededRepository {

  private final SpringCmdProceededRepository springCmdProceededRepository;

  private final EntityManager entityManager;

  public JpaCmdProceededRepository(
      SpringCmdProceededRepository springCmdProceededRepository,
      EntityManager entityManager) {
    this.springCmdProceededRepository = springCmdProceededRepository;
    this.entityManager = entityManager;
  }

  public List<CmdProceeded> findTop1000OutDated(long date) {
    var cmdProceedList = springCmdProceededRepository
        .findTop1000ByCreatedAtBeforeOrderByCreatedAt(OffsetDateTime.now().minusDays(date));

    log.info("method: findTop1000OutDated, date: {} , cmdProceedListSize: {}", date,
        cmdProceedList.size());

    return cmdProceedList;
  }

  public void deleteAll(Collection<CmdProceeded> cmdProceededs) {
    springCmdProceededRepository.deleteAll(cmdProceededs);

    log.info("method: deleteAll, cmdProceedsSize: {}", cmdProceededs.size());
  }

  public void deDup(String cmdId) {
    if (springCmdProceededRepository.existsById(cmdId)) {
      throw new DuplicateException(String.format("Command %s was already processed", cmdId));
    }

    entityManager.persist(new CmdProceeded().setCmdProceededId(cmdId));

    log.info("method: create, cmdId: {}", cmdId);
  }
}
