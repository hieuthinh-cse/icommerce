package vn.icommerce.icommerce.infra.rest;

public abstract class AbstractIntegrationTest {

  protected static final boolean PIPELINE_ENV = "true"
      .equals(System.getProperty("it.pipelineEnv", "false"));

  static {
    System.setProperty("spring.profiles.active", "local");

    if (PIPELINE_ENV) {
      System.setProperty("logging.config", "classpath:log4j2-pipelineEnv.xml");
    }
  }
}
