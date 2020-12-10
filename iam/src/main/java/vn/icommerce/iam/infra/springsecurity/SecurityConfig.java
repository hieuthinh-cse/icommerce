package vn.icommerce.iam.infra.springsecurity;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vn.icommerce.common.jackson.JacksonExecutor;

/**
 * Configures the security for this service.
 */
@Data
@EnableWebSecurity
@EnableJpaAuditing(
    dateTimeProviderRef = "offsetDateTimeProvider")
@ConfigurationProperties("iam.infra.springsecurity")
public class SecurityConfig {

  /**
   * The BASE64-encoded JWT key.
   */
  private String jwtKey = "***";

  /**
   * The username to access the monitor endpoints.
   */
  private String username = "monitor";

  /**
   * The password to access the monitor endpoints.
   */
  private String password = "***";

  /**
   * The role to access the monitor endpoints.
   */
  private String role = "MONITOR";

  private final JacksonExecutor jacksonExecutor;

  /**
   * The api key pairs.
   */
  private String apiKeysStore = "{}";

  private Map<String, String> apiKeysStoreAsMap() {
    return jacksonExecutor.deserializeFromString(
        apiKeysStore,
        new TypeReference<>() {
        });
  }

  @Configuration
  @Order(SecurityProperties.BASIC_AUTH_ORDER - 9)
  public static class BuyerSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthTokenFilter authTokenFilter;

    private final ForbiddenRequestHandler forbiddenRequestHandler;

    private final UnauthorizedRequestHandler unauthorizedRequestHandler;

    public BuyerSecurityConfig(
        JwtTokenCryptoEngine jwtTokenCryptoEngine,
        ForbiddenRequestHandler forbiddenRequestHandler,
        UnauthorizedRequestHandler unauthorizedRequestHandler) {
      this.authTokenFilter = new AuthTokenFilter(jwtTokenCryptoEngine);
      this.forbiddenRequestHandler = forbiddenRequestHandler;
      this.unauthorizedRequestHandler = unauthorizedRequestHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
          .antMatcher("/v?/**")
          .authorizeRequests()
          .antMatchers(
              "/v?/tokens/**")
          .permitAll()
          .antMatchers(HttpMethod.GET, "/v?/me")
          .authenticated()
          .and()
          .exceptionHandling()
          .accessDeniedHandler(forbiddenRequestHandler)
          .authenticationEntryPoint(unauthorizedRequestHandler)
          .and()
          .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          .cors().disable().csrf().disable();
    }
  }

  /**
   * Configures the security for the internal flow.
   */
  @Configuration
  @Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
  public static class InternalSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthApiKeyFilter authApiKeyFilter;

    private final UnauthorizedRequestHandler unauthorizedRequestHandler;

    public InternalSecurityConfig(
        SecurityConfig securityConfig,
        UnauthorizedRequestHandler unauthorizedRequestHandler) {
      this.authApiKeyFilter = new AuthApiKeyFilter(securityConfig.apiKeysStoreAsMap());
      this.unauthorizedRequestHandler = unauthorizedRequestHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
          .antMatcher("/v?/internal/**")
          .authorizeRequests()
          .anyRequest().authenticated()
          .and()
          .exceptionHandling().authenticationEntryPoint(unauthorizedRequestHandler)
          .and()
          .addFilterBefore(authApiKeyFilter, UsernamePasswordAuthenticationFilter.class)
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          .cors().disable().csrf().disable();
    }
  }

  /**
   * Configures the security for monitor endpoints.
   */
  @Configuration
  @Order(SecurityProperties.BASIC_AUTH_ORDER - 6)
  public static class MonitorSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityConfig securityConfig;

    private final PasswordEncoder passwordEncoder;

    public MonitorSecurityConfig(
        PasswordEncoder passwordEncoder,
        SecurityConfig securityConfig) {
      this.passwordEncoder = passwordEncoder;
      this.securityConfig = securityConfig;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth
          .inMemoryAuthentication()
          .withUser(securityConfig.getUsername())
          .password(passwordEncoder.encode(securityConfig.getPassword()))
          .roles(securityConfig.getRole());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
          .requestMatchers()
          .and()
          .httpBasic()
          .and()
          .authorizeRequests()
          .antMatchers("/monitor/health").permitAll()
          .anyRequest().authenticated()
          .and()
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          .cors().disable().csrf().disable();
    }

  }
}