/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.sharedkernel.app.component;

import java.util.function.Supplier;

/**
 * Interface to the transaction manager that manages the transaction life cycle.
 *
 * <p>Created on 10/19/19.
 *
 * @author khoanguyenminh
 */
public interface TxManager {

  <T> T doInReadOnlyTx(Supplier<T> supplier);

  void doInTx(Runnable runnable);

  <T> T doInTx(Supplier<T> supplier);
}
