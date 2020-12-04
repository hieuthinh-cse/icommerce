/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.infra.springsecurity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.stereotype.Component;
import vn.icommerce.icommerce.app.component.TokenCryptoEngine;
import vn.icommerce.sharedkernel.domain.model.BuyerToken;

/**
 * Implementation that use JWT to sign the token.
 *
 * <p>Created on 8/27/19.
 *
 * @author khoanguyenminh
 */
@Component
public class JwtTokenCryptoEngine implements TokenCryptoEngine {

  private static final String ROLE_KEY = "rol";

  private static final String PRIVILEGE_KEY = "pri";

  private final SecurityConfig securityConfig;

  public JwtTokenCryptoEngine(SecurityConfig securityConfig) {
    this.securityConfig = securityConfig;
  }

  @Override
  public String signAccountToken(BuyerToken buyerToken) {
    return Jwts
        .builder()
        .signWith(SignatureAlgorithm.HS512, securityConfig.getJwtKey())
        .setSubject(buyerToken.getBuyerId().toString())
        .setId(buyerToken.getSessionId())
        .setExpiration(Date.from(buyerToken.getExpiredAt().toInstant()))
        .compact();
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
