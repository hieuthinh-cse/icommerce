package vn.icommerce.icommerce.infra.springsecurity;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.icommerce.icommerce.app.component.TokenCryptoEngine;
import vn.icommerce.sharedkernel.domain.model.BuyerToken;

/**
 * This filter perform authentication via token.
 */
public class AuthTokenFilter extends OncePerRequestFilter {

  private static final String BEARER_SCHEME = "Bearer ";

  private static final String AUTHORIZATION = "Authorization";

  private static final String CORRELATION_ID = "Correlation-Id";

  private final TokenCryptoEngine tokenCryptoEngine;

  public AuthTokenFilter(TokenCryptoEngine tokenCryptoEngine) {
    this.tokenCryptoEngine = tokenCryptoEngine;
  }

  private UsernamePasswordAuthenticationToken createAuth(
      HttpServletRequest request,
      BuyerToken buyerToken) {
    var auth = new UsernamePasswordAuthenticationToken(
        buyerToken,
        null,
        null);

    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    return auth;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    MDC.put("correlation_id", request.getHeader(CORRELATION_ID));

    try {
      var token = tokenCryptoEngine
          .verifyAccountToken(request.getHeader(AUTHORIZATION).substring(BEARER_SCHEME.length()));

      SecurityContextHolder
          .getContext()
          .setAuthentication(createAuth(request, token));
    } catch (ExpiredJwtException e) {
      request.setAttribute("expired", true);
    } catch (Exception e) {
      request.removeAttribute("expired");
    }

    filterChain.doFilter(request, response);
  }
}
