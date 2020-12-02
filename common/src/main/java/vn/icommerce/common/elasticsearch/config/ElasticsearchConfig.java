/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.elasticsearch.config;

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
 * <p>Created on 9/21/19.
 *
 * @author khoanguyenminh
 */
@Configuration
@Data
@ConfigurationProperties("common.infra.elasticsearch")
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
   * The Elasticsearch max connection per route.
   */
  private int maxConnectionPerRoute = 10;

  /**
   * The Elasticsearch max connection total.
   */
  private int maxConnectionTotal = 30;

  /**
   * The Elasticsearch connect timeout in ms.
   */
  private int connectTimeoutMs = 5_000;

  /**
   * The Elasticsearch read timeout in ms.
   */
  private int readTimeoutMs = 5_000;

  /**
   * The Elasticsearch connection request timeout in ms.
   */
  private int connectionRequestTimeoutMs = 5_000;

  /**
   * The Elasticsearch search limit total.
   */
  private int limitTotal = 10_000;

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
          if (Objects.nonNull(username) && Objects.nonNull(password)) {
            var credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));
            httpClientBuilder
                .setDefaultCredentialsProvider(credentialsProvider);
          }

          return httpClientBuilder
              .setMaxConnPerRoute(maxConnectionPerRoute)
              .setMaxConnTotal(maxConnectionTotal);
        })
        .setRequestConfigCallback(requestConfigBuilder ->
            requestConfigBuilder
                .setConnectionRequestTimeout(connectionRequestTimeoutMs)
                .setConnectTimeout(connectTimeoutMs)
                .setSocketTimeout(readTimeoutMs)
        );

    return new RestHighLevelClient(builder);
  }

  @Bean
  public RestClient restClient() {
    return restHighLevelClient().getLowLevelClient();
  }
}
