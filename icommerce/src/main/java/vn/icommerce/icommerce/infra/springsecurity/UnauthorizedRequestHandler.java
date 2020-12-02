/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.infra.springsecurity;

import static vn.icommerce.sharedkernel.domain.model.DomainCode.TOKEN_EXPIRED;
import static vn.icommerce.sharedkernel.domain.model.DomainCode.UNAUTHORIZED;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.entity.ContentType;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import vn.icommerce.common.jackson.JacksonExecutor;
import vn.icommerce.icommerce.infra.rest.ApiResp;

/**
 * Handles unauthorized request.
 *
 * <p>Created on 11/22/19.
 *
 * @author khoanguyenminh
 */
@Component
public class UnauthorizedRequestHandler implements AuthenticationEntryPoint {

  private final MessageSource messageSource;

  private final JacksonExecutor jacksonExecutor;

  public UnauthorizedRequestHandler(
      MessageSource messageSource,
      JacksonExecutor jacksonExecutor) {
    this.messageSource = messageSource;
    this.jacksonExecutor = jacksonExecutor;
  }

  private ApiResp getApiResp(HttpServletRequest request) {
    return Optional
        .ofNullable(request.getAttribute("expired"))
        .map(expired -> new ApiResp()
            .setCode(TOKEN_EXPIRED.value())
            .setMessage(
                messageSource.getMessage(TOKEN_EXPIRED.valueAsString(), null, request.getLocale())))
        .orElseGet(() -> new ApiResp()
            .setCode(UNAUTHORIZED.value())
            .setMessage(
                messageSource.getMessage(UNAUTHORIZED.valueAsString(), null, request.getLocale())));
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    response.setContentType(ContentType.APPLICATION_JSON.toString());
    var out = response.getOutputStream();
    jacksonExecutor.serializeToStream(response.getOutputStream(), getApiResp(request));
    out.flush();
  }
}
