/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.springtx;

import java.util.function.Supplier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation that uses the Spring annotation to perform the logic.
 *
 * <p>Created on 10/19/19.
 *
 * @author khoanguyenminh
 */
@Component
public class SpringTxExecutor {

  @Transactional(readOnly = true)
  public void doInReadOnlyTx(Runnable runnable) {
    runnable.run();
  }

  @Transactional(readOnly = true)
  public <T> T doInReadOnlyTx(Supplier<T> supplier) {
    return supplier.get();
  }

  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void doInTx(Runnable runnable) {
    runnable.run();
  }

  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public <T> T doInTx(Supplier<T> supplier) {
    return supplier.get();
  }

  @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
  public void doInNewTx(Runnable runnable) {
    runnable.run();
  }

  @Transactional(transactionManager = "chainedKafkaTransactionManager")
  public void doKafkaInTx(Runnable runnable) {
    runnable.run();
  }
}
