/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.infra.springsecurity;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.icommerce.icommerce.app.component.AccountInfoHolder;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.AccountToken;
import vn.icommerce.sharedkernel.domain.model.DomainCode;

/**
 * Implementation that uses the security context to perform the business logic.
 *
 */
@Component
public class ContextAccountInfoHolder implements AccountInfoHolder {

  @Override
  public Long getAccountId() {
    return Optional
        .ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(Authentication::getPrincipal)
        .map(AccountToken.class::cast)
        .map(AccountToken::getAccountId)
        .orElseThrow(() -> new DomainException(DomainCode.UNAUTHORIZED));
  }

  @Override
  public String getSessionId() {
    return Optional
        .ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(Authentication::getPrincipal)
        .map(AccountToken.class::cast)
        .map(AccountToken::getSessionId)
        .orElseThrow(() -> new DomainException(DomainCode.UNAUTHORIZED));
  }

}
