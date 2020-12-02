/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.jackson;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Configures the object mapper.
 *
 * <p>Created on 12/7/19.
 *
 * @author khoanguyenminh
 */
@Configuration
public class JacksonConfig {

  @Bean
  public ObjectMapper objectMapper() {
    var simpleModule = new SimpleModule("Senpay")
        .addSerializer(BigDecimal.class, new BigDecimalSerializer())
        .addDeserializer(BigDecimal.class, new BigDecimalDeserializer())
        .addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer())
        .addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer())
        .addSerializer(Number.class, new NumberSerializer());

    return new ObjectMapper()
        .setDefaultPropertyInclusion(Include.NON_ABSENT)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(simpleModule);
  }

  /**
   * Defines the converter to deal with the responses replied to the client-side app of the Spring
   * Boot Admin. It requires the responses with the value of type number instead of string.
   *
   * @return the converter with the normal JSON deserializer.
   */
  @Bean
  @Order(0)
  public MappingJackson2HttpMessageConverter actuatorJacksonHttpMessageConverter() {
    var objectMapper = new ObjectMapper()
        .setDefaultPropertyInclusion(Include.NON_ABSENT)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    return new MappingJackson2HttpMessageConverter(objectMapper) {

      private final MediaType supportedMediaType = new MediaType(
          "application",
          "vnd.spring-boot.actuator.v2+json");

      @Override
      protected boolean canWrite(MediaType mediaType) {
        return supportedMediaType.equals(mediaType);
      }

      @Override
      protected boolean canRead(MediaType mediaType) {
        return false;
      }
    };
  }

  /**
   * Defines the converter to work with normal business flow.
   *
   * @return the converter with the enhanced JSON mapper.
   */
  @Bean
  @Order(1)
  public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
    return new MappingJackson2HttpMessageConverter(objectMapper());
  }

}
