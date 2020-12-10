

package vn.icommerce.sharedkernel.app.component;

import java.util.function.Supplier;

/**
 * Interface to the transaction manager that manages the transaction life cycle.
 *
 *
 *
 *
 */
public interface TxManager {

  <T> T doInReadOnlyTx(Supplier<T> supplier);

  void doInTx(Runnable runnable);

  <T> T doInTx(Supplier<T> supplier);
}
