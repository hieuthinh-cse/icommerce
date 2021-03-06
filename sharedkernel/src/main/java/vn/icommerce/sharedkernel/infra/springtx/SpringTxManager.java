

package vn.icommerce.sharedkernel.infra.springtx;

import java.util.function.Supplier;
import org.springframework.stereotype.Component;
import vn.icommerce.common.springtx.SpringTxExecutor;
import vn.icommerce.sharedkernel.app.component.TxManager;

/**
 * Implementation that uses the Spring annotation to perform the logic.
 *
 *
 *
 *
 */
@Component
public class SpringTxManager implements TxManager {

  private final SpringTxExecutor springTxExecutor;

  public SpringTxManager(
      SpringTxExecutor springTxExecutor) {
    this.springTxExecutor = springTxExecutor;
  }

  @Override
  public <T> T doInReadOnlyTx(Supplier<T> supplier) {
    return springTxExecutor.doInReadOnlyTx(supplier);
  }

  @Override
  public void doInTx(Runnable runnable) {
    springTxExecutor.doInTx(runnable);
  }

  @Override
  public <T> T doInTx(Supplier<T> supplier) {
    return springTxExecutor.doInTx(supplier);
  }
}
