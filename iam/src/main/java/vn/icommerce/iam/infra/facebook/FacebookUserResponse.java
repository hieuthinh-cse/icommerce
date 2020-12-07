package vn.icommerce.iam.infra.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.icommerce.iam.app.component.FacebookPicture;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacebookUserResponse {

  private String id;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  private String email;
  
  private FacebookPicture picture;
}