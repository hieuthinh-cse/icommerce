/*
 * Copyright 2020 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.infra.springsecurity;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.icommerce.icommerce.app.component.OperatorInfoHolder;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.DomainCode;
import vn.icommerce.sharedkernel.domain.model.OptToken;

/**
 * <p>Created on 06/22/2020.
 *
 * @author tuanlt2
 */
@Component
public class ContextOperatorInfoHolder implements OperatorInfoHolder {

  @Override
  public String getEmail() {
    return Optional
        .ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(Authentication::getPrincipal)
        .map(OptToken.class::cast)
        .map(OptToken::getEmail)
        .orElseThrow(() -> new DomainException(DomainCode.UNAUTHORIZED));
  }
}
