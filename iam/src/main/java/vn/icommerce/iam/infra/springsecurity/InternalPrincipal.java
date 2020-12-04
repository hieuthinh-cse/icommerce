package vn.icommerce.iam.infra.springsecurity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import vn.icommerce.sharedkernel.domain.model.Partner;

/**
 * Implementation to get information after authentication for manipulation API.
 *
 */
@Getter
@Setter
@Accessors(chain = true)
public class InternalPrincipal {

  private String apiKey;

  public String getAuthor() {
    return Partner.getNameByValue(apiKey);
  }
}
