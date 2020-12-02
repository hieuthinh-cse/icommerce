package vn.icommerce;

import java.util.Optional;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;

public class RedisInitializer extends AbstractItInitializer {

  private static GenericContainer redisContainer;

  static {
    if (PIPELINE_ENV) {
      startRedisContainer();
    }
  }

  private static void startRedisContainer() {
    redisContainer = new GenericContainer("redis:latest");
    redisContainer.start();
  }

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    Optional
        .ofNullable(redisContainer)
        .ifPresent(container -> useContainerForRedisTest(applicationContext));
  }

  private void useContainerForRedisTest(ConfigurableApplicationContext applicationContext) {
    TestPropertyValues
        .of(
            "iam.infra.redis.host=" + redisContainer.getContainerIpAddress(),
            "iam.infra.redis.port=" + redisContainer.getMappedPort(6379))
        .applyTo(applicationContext.getEnvironment());
  }
}
