package vn.icommerce.icommerce.infra.springsecurity;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
 *
 * <p>Created on 11/21/19.
 *
 * @author khoanguyenminh
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

  /**
   * Configures the security for the operator flow.
   */
  @Configuration
  @Order(SecurityProperties.BASIC_AUTH_ORDER - 9)
  public static class OperatorSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthOptTokenFilter authOptTokenFilter;

    private final ForbiddenRequestHandler forbiddenRequestHandler;

    private final UnauthorizedRequestHandler unauthorizedRequestHandler;

    public OperatorSecurityConfig(
        JwtTokenCryptoEngine jwtTokenCryptoEngine,
        ForbiddenRequestHandler forbiddenRequestHandler,
        UnauthorizedRequestHandler unauthorizedRequestHandler) {
      this.authOptTokenFilter = new AuthOptTokenFilter(jwtTokenCryptoEngine);
      this.forbiddenRequestHandler = forbiddenRequestHandler;
      this.unauthorizedRequestHandler = unauthorizedRequestHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
          .antMatcher("/v?/opt/**")
          .authorizeRequests()

          .antMatchers(HttpMethod.GET, "/v?/opt/merchants/**")
          .hasAuthority("REA_MER")

          .antMatchers(HttpMethod.POST, "/v?/opt/merchants/**")
          .hasAuthority("WRI_MER")

          .antMatchers(HttpMethod.PUT, "/v?/opt/merchants/**/lock", "/v?/opt/merchants/**/unlock")
          .hasAuthority("LOC_MER")

          .antMatchers(HttpMethod.PUT, "/v?/opt/merchants/**/delete")
          .hasAuthority("DEL_MER")

          .antMatchers(HttpMethod.PUT, "/v?/opt/merchants/**")
          .hasAuthority("WRI_MER")

          .antMatchers(HttpMethod.GET, "/v?/opt/accounts/**")
          .hasAuthority("REA_ACC")

          .and()
          .exceptionHandling()
          .accessDeniedHandler(forbiddenRequestHandler)
          .authenticationEntryPoint(unauthorizedRequestHandler)
          .and()
          .addFilterBefore(authOptTokenFilter, UsernamePasswordAuthenticationFilter.class)
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

    /**
     * Configures no security in case of local development.
     */
    @Profile("local")
    @Configuration
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 100)
    public static class NoOpSecurityConfig extends WebSecurityConfigurerAdapter {

      @Override
      protected void configure(HttpSecurity http) throws Exception {
        http
            .requestMatchers()
            .and()
            .authorizeRequests().anyRequest().permitAll()
            .and()
            .cors().disable().csrf().disable();
      }
    }
}