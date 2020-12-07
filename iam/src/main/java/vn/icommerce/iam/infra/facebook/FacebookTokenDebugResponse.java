package vn.icommerce.iam.infra.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacebookTokenDebugResponse {

  private TokenDebugData data;

  @Data
  @Accessors(chain = true)
  public static class TokenDebugData {

    private ErrorData error;

    @JsonProperty("app_id")
    private String appId;

    @JsonProperty("is_valid")
    private Boolean isValid;

    @Data
    @Accessors(chain = true)
    public static class ErrorData {

      private String code;

      private String message;
    }

  }


}