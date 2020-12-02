/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.infra.springsecurity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.springframework.stereotype.Component;
import vn.icommerce.icommerce.app.component.TokenCryptoEngine;
import vn.icommerce.sharedkernel.domain.model.AccountToken;
import vn.icommerce.sharedkernel.domain.model.OptToken;

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
  public String signAccountToken(AccountToken accountToken) {
    return Jwts
        .builder()
        .signWith(SignatureAlgorithm.HS512, securityConfig.getJwtKey())
        .setSubject(accountToken.getAccountId().toString())
        .setId(accountToken.getSessionId())
        .setExpiration(Date.from(accountToken.getExpiredAt().toInstant()))
        .compact();
  }

  @Override
  public AccountToken verifyAccountToken(String encodedToken) {
    var claims = Jwts
        .parser()
        .setSigningKey(securityConfig.getJwtKey())
        .parseClaimsJws(encodedToken)
        .getBody();

    return new AccountToken()
        .setAccountId(Long.valueOf(claims.getSubject()))
        .setSessionId(claims.getId());
  }

  @Override
  public String signOptToken(OptToken optToken) {
    return Jwts
        .builder()
        .signWith(SignatureAlgorithm.HS512, securityConfig.getJwtKey())
        .setSubject(optToken.getEmail())
        .setExpiration(Date.from(optToken.getExpiredAt().toInstant()))
        .claim(ROLE_KEY, optToken.getRole())
        .claim(PRIVILEGE_KEY, optToken.getPrivilegeSet())
        .compact();
  }

  @Override
  public OptToken verifyOptToken(String encodedToken) {
    var claims = Jwts
        .parser()
        .setSigningKey(securityConfig.getJwtKey())
        .parseClaimsJws(encodedToken)
        .getBody();

    @SuppressWarnings("unchecked")
    var privilegeSet = new HashSet<String>(claims.get(PRIVILEGE_KEY, List.class));

    return new OptToken()
        .setEmail(claims.getSubject())
        .setRole(claims.get(ROLE_KEY, String.class))
        .setPrivilegeSet(privilegeSet);
  }
}
