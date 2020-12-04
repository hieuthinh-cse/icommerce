package vn.icommerce.iam.infra.springsecurity;

import static vn.icommerce.sharedkernel.domain.model.DomainCode.FORBIDDEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.entity.ContentType;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import vn.icommerce.iam.infra.rest.ApiResp;

/**
 * Handles forbidden request.
 */
@Component
public class ForbiddenRequestHandler implements AccessDeniedHandler {

  private final MessageSource messageSource;

  private final ObjectMapper objectMapper;

  public ForbiddenRequestHandler(
      MessageSource messageSource,
      ObjectMapper objectMapper) {
    this.messageSource = messageSource;
    this.objectMapper = objectMapper;
  }

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {
    var apiResp = new ApiResp()
        .setCode(FORBIDDEN.value())
        .setMessage(messageSource.getMessage(FORBIDDEN.valueAsString(), null, request.getLocale()));

    response.setContentType(ContentType.APPLICATION_JSON.toString());
    var out = response.getOutputStream();
    objectMapper.writeValue(out, apiResp);
    out.flush();
  }
}
