package vn.icommerce.icommerce.infra.springsecurity;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.icommerce.icommerce.app.component.BuyerInfoHolder;
import vn.icommerce.sharedkernel.domain.exception.DomainException;
import vn.icommerce.sharedkernel.domain.model.BuyerToken;
import vn.icommerce.sharedkernel.domain.model.DomainCode;

/**
 * Implementation that uses the security context to perform the business logic.
 */
@Component
public class ContextBuyerInfoHolder implements BuyerInfoHolder {

  @Override
  public Boolean isLogin() {
    return SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal() instanceof BuyerToken;
  }

  @Override
  public Long getBuyerId() {
    return Optional
        .ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(Authentication::getPrincipal)
        .map(BuyerToken.class::cast)
        .map(BuyerToken::getBuyerId)
        .orElseThrow(() -> new DomainException(DomainCode.UNAUTHORIZED));
  }

  @Override
  public String getSessionId() {
    return Optional
        .ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(Authentication::getPrincipal)
        .map(BuyerToken.class::cast)
        .map(BuyerToken::getSessionId)
        .orElseThrow(() -> new DomainException(DomainCode.UNAUTHORIZED));
  }

}
