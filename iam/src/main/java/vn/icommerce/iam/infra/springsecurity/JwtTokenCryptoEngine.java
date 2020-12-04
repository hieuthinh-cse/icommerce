package vn.icommerce.iam.infra.springsecurity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.stereotype.Component;
import vn.icommerce.iam.app.component.TokenCryptoEngine;
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
  public String signBuyerToken(BuyerToken token) {
    return Jwts
        .builder()
        .signWith(SignatureAlgorithm.HS512, securityConfig.getJwtKey())
        .setSubject(token.getBuyerId().toString())
        .setId(token.getSessionId())
//        .setIssuer(token.getScope().getMsg())
        .setExpiration(Date.from(token.getExpiredAt().toInstant()))
        .compact();
  }

  @Override
  public BuyerToken verifyBuyerToken(String encodedToken) {
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
