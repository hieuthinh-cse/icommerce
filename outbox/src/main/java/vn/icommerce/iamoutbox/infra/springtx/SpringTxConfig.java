

package vn.icommerce.iamoutbox.infra.springtx;

import javax.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;

/**
 * Configures the transaction manager.
 *
 *
 *
 *
 */
@Configuration
public class SpringTxConfig {

  @Bean
  @Primary
  public JpaTransactionManager transactionManager(EntityManagerFactory entityManager) {
    return new JpaTransactionManager(entityManager);
  }
}
