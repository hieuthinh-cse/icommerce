package vn.icommerce.icommerce;

import java.io.IOException;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import vn.icommerce.common.elasticsearch.ElasticsearchMarker;
import vn.icommerce.common.jackson.JacksonMarker;
import vn.icommerce.sharedkernel.infra.elasticsearch.ElasticsearchSearchEngine;
import vn.icommerce.EsItInitializer;

/**
 * JPA integration test parent class to leverage context cache.
 *
 * <p>Created on 5/5/20.
 *
 * @author thinh.nguyenhieu
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = EsItInitializer.class)
@EnableConfigurationProperties
public abstract class EsIamTest {

  @TestConfiguration
  @ComponentScan(basePackageClasses = {
      ElasticsearchSearchEngine.class,
      ElasticsearchMarker.class,
      JacksonMarker.class
  })

  public static class TestConfig {

  }

  @Autowired
  protected ElasticsearchSearchEngine searchEngine;

  @Autowired
  private RestHighLevelClient client;

  protected void flushAndRefresh() throws IOException {
    client.indices().flush(new FlushRequest(), RequestOptions.DEFAULT);
    client.indices().refresh(new RefreshRequest(), RequestOptions.DEFAULT);
  }
}
