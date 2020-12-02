/*
 * Copyright 2020 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.infra.argon2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

/**
 * Bean configuration for Argon2 hashing function.
 *
 */
@Configuration
public class Argon2Config {

  @Bean
  public Argon2PasswordEncoder argon2PasswordEncoder() {
    return new Argon2PasswordEncoder();
  }
}
