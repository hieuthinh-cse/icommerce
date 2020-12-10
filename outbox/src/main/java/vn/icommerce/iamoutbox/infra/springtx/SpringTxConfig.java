/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.iamoutbox.infra.springtx;

import javax.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;

/**
 * Configures the transaction manager.
 *
 * <p>Created on 11/21/19.
 *
 * @author khoanguyenminh
 */
@Configuration
public class SpringTxConfig {

  @Bean
  @Primary
  public JpaTransactionManager transactionManager(EntityManagerFactory entityManager) {
    return new JpaTransactionManager(entityManager);
  }
}
