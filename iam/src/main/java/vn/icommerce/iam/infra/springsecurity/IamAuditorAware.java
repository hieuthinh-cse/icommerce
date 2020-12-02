/*
 * Copyright 2020 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.iam.infra.springsecurity;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.icommerce.sharedkernel.domain.model.BuyerToken;

/**
 * <p>Created on 06/15/2020.
 *
 * @author tuanlt2
 */

@Component
public class IamAuditorAware implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication instanceof UsernamePasswordAuthenticationToken) {
      var principal = authentication.getPrincipal();

      if (principal instanceof BuyerToken) {
        return Optional.of(((BuyerToken) principal).getBuyerId().toString());
      } else {
        return Optional.of("User");
      }
    }

    return Optional.of("System");
  }
}
