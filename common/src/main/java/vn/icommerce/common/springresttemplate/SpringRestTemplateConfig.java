/*
 * Copyright 2020 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.springresttemplate;

import lombok.Data;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * <p>Created on 5/12/20.
 *
 * @author khoanguyenminh
 */
@Data
@Configuration
@ConfigurationProperties("common.infra.springresttemplate")
public class SpringRestTemplateConfig {

  /**
   * Max connection total.
   */
  private int maxConnectionTotal = 200;

  /**
   * Max connection per route.
   */
  private int maxConnectionPerRoute = 20;

  /**
   * Connect timeout in ms.
   */
  private int connectTimeoutMs = 5_000;

  /**
   * Read timeout (between received packets) in ms.
   */
  private int readTimeoutMs = 20_000;

  /**
   * Connection requested from pool timeout in ms.
   */
  private int connectionRequestTimeoutMs = 5_000;

  /**
   * Configures the REST client.
   *
   * @return the configured client
   */
  @Bean
  public RestTemplate restTemplate(
      RestTemplateBuilder restTemplateBuilder,
      MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
    return restTemplateBuilder
        .requestFactory(this::httpComponentsClientHttpRequestFactory)
        .messageConverters(mappingJackson2HttpMessageConverter)
        .build();
  }

  @Bean
  public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory() {
    var clientHttpRequestFactory =
        new HttpComponentsClientHttpRequestFactory(closeableHttpClient());
    clientHttpRequestFactory.setConnectTimeout(connectTimeoutMs);
    clientHttpRequestFactory.setReadTimeout(readTimeoutMs);
    clientHttpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeoutMs);

    return clientHttpRequestFactory;
  }

  private CloseableHttpClient closeableHttpClient() {
    return HttpClients
        .custom()
        .disableAuthCaching()
        .disableCookieManagement()
        .disableRedirectHandling()
        .disableDefaultUserAgent()
        .disableConnectionState()
        .setMaxConnTotal(maxConnectionTotal)
        .setMaxConnPerRoute(maxConnectionPerRoute)
        .evictExpiredConnections()
        .useSystemProperties()
        .build();
  }

}
