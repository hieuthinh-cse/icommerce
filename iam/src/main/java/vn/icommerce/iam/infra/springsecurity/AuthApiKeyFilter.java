

package vn.icommerce.iam.infra.springsecurity;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This filter perform authentication via API key.
 *
 *
 *
 *
 */
public class AuthApiKeyFilter extends OncePerRequestFilter {

  private static final String API_KEY = "Api-Key";

  private static final String SIGNATURE = "Signature";

  private static final String CORRELATION_ID = "Correlation-Id";

  private final Map<String, String> apiKeysStore;

  public AuthApiKeyFilter(Map<String, String> apiKeysStore) {
    this.apiKeysStore = apiKeysStore;
  }

  private boolean hasValidApiKey(HttpServletRequest request) {
    String apiKey = request.getHeader(API_KEY);
    String correlationId = request.getHeader(CORRELATION_ID);
    String signature = request.getHeader(SIGNATURE);

    return Objects.nonNull(apiKey)
        && Objects.nonNull(correlationId)
        && Objects.nonNull(signature)
        && apiKeysStore.containsKey(apiKey)
        && DigestUtils.sha256Hex(apiKeysStore.get(apiKey) + "|" + correlationId)
        .equals(signature);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    MDC.put("correlation_id", request.getHeader(CORRELATION_ID));

    if (hasValidApiKey(request)) {
      SecurityContextHolder
          .getContext()
          .setAuthentication(new UsernamePasswordAuthenticationToken(
              new InternalPrincipal().setApiKey(request.getHeader(API_KEY)),
              null,
              Collections.emptyList()));
    }

    filterChain.doFilter(request, response);
  }
}
