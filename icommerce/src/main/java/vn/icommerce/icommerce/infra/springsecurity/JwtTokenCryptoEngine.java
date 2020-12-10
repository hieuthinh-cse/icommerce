package vn.icommerce.icommerce.infra.springsecurity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.stereotype.Component;
import vn.icommerce.icommerce.app.component.TokenCryptoEngine;
import vn.icommerce.sharedkernel.domain.model.BuyerToken;

/**
 * Implementation that use JWT to sign the token.
 */
@Component
public class JwtTokenCryptoEngine implements TokenCryptoEngine {

  private final SecurityConfig securityConfig;

  public JwtTokenCryptoEngine(SecurityConfig securityConfig) {
    this.securityConfig = securityConfig;
  }

  @Override
  public BuyerToken verifyAccountToken(String encodedToken) {
    var claims = Jwts
        .parser()
        .setSigningKey(securityConfig.getJwtKey())
        .parseClaimsJws(encodedToken)
        .getBody();

    return new BuyerToken()
        .setBuyerId(Long.valueOf(claims.getSubject()))
        .setSessionId(claims.getId());
  }
}
