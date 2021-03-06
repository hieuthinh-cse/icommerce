package vn.icommerce.dbtool.config;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for Elasticsearch REST high level client.
 *
 */
@Configuration
@Data
@ConfigurationProperties("dbtool.infra.elasticsearch")
public class ElasticsearchConfig {

  /**
   * The Elasticsearch cluster host.
   */
  private List<String> hosts = Collections.singletonList("http://localhost:9200");

  /**
   * The Elasticsearch username.
   */
  private String username = null;

  /**
   * The Elasticsearch password.
   */
  private String password = null;

  /**
   * The Elasticsearch connect timeout in ms.
   */
  private int connectTimeoutMs = 5000;

  /**
   * The Elasticsearch read timeout in ms.
   */
  private int readTimeoutMs = 5000;

  /**
   * The Elasticsearch connection request timeout in ms.
   */
  private int connectionRequestTimeoutMs = 5000;

  /**
   * Configures a {@link RestHighLevelClient} bean.
   *
   * @return the configured Elasticsearch client
   */
  @Bean
  @Primary
  public RestHighLevelClient restHighLevelClient() {
    var builder = RestClient
        .builder(hosts
            .stream()
            .map(HttpHost::create)
            .toArray(HttpHost[]::new))
        .setHttpClientConfigCallback(httpClientBuilder -> {
          if (Objects.nonNull(username)
              && Objects.nonNull(password)) {
            var credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));
            httpClientBuilder
                .setDefaultCredentialsProvider(credentialsProvider);
          }

          return httpClientBuilder;
        })
        .setRequestConfigCallback(requestConfigBuilder ->
            requestConfigBuilder
                .setConnectionRequestTimeout(connectionRequestTimeoutMs)
                .setConnectTimeout(connectTimeoutMs)
                .setSocketTimeout(readTimeoutMs));

    return new RestHighLevelClient(builder);
  }

  @Bean
  public RestClient restClient() {
    return restHighLevelClient().getLowLevelClient();
  }
}
