package vn.icommerce.icommerce;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import vn.icommerce.JpaItInitializer;
import vn.icommerce.common.jackson.JacksonMarker;
import vn.icommerce.common.outbox.Outbox;
import vn.icommerce.common.outbox.OutboxMarker;
import vn.icommerce.common.outbox.SpringOutboxRepository;
import vn.icommerce.common.springtx.SpringTxMarker;
import vn.icommerce.sharedkernel.SharedKernelMarker;
import vn.icommerce.sharedkernel.infra.jpa.JpaMarker;
import vn.icommerce.sharedkernel.infra.jpaoutbox.JpaOutboxEngine;

/**
 * JPA integration test parent class to leverage context cache.
 *
 * <p>Created on 3/17/20.
 *
 * @author tuanlethanh
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(initializers = JpaItInitializer.class)
public abstract class JpaIamTest {

  @TestConfiguration
  @ComponentScan(basePackageClasses = {
      JacksonMarker.class,
      SpringTxMarker.class,
      OutboxMarker.class,
      JpaOutboxEngine.class,
      JpaMarker.class
  })
  @EnableJpaRepositories(basePackageClasses = {JpaMarker.class, OutboxMarker.class})
  @EntityScan(basePackageClasses = {SharedKernelMarker.class, OutboxMarker.class})

  public static class TestConfig {

  }

  @Autowired
  protected TestEntityManager testEntityManager;

  protected void flushAndClear() {
    try {
      testEntityManager.flush();
    } finally {
      testEntityManager.clear();
    }
  }

  protected <T> T find(Class<T> clazz, Object key) {
    return testEntityManager.find(clazz, key);
  }

  @Autowired
  private SpringOutboxRepository springOutboxRepository;

  protected Page<Outbox> findLastOutboxes(int size) {
    var pageable = PageRequest.of(0, size, Sort.by(Order.desc("createdAt")));

    return springOutboxRepository.findAll(pageable);
  }

}
