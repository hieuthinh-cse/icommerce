

package vn.icommerce.icommerce.infra.springsecurity;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.icommerce.sharedkernel.domain.model.BuyerToken;

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
