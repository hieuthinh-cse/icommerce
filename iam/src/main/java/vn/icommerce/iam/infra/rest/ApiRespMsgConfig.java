/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.iam.infra.rest;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Message resource configuration for iam service.
 *
 * <p>Created on 10/4/19.
 *
 * @author khoanguyenminh
 */
@Configuration
public class ApiRespMsgConfig {

  /**
   * Configures the {@link MessageSource} for i18n.
   *
   * @return the {@link MessageSource}
   */
  @Bean
  public MessageSource messageSource() {
    var messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");

    return messageSource;
  }

  /**
   * Configures the custom validation message source.
   *
   * @return the validator factory bean with custom message source
   */
  @Bean
  public LocalValidatorFactoryBean getValidator() {
    var localValidatorFactoryBean = new LocalValidatorFactoryBean();
    localValidatorFactoryBean.setValidationMessageSource(messageSource());

    return localValidatorFactoryBean;
  }
}
