/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.infra.springsecurity;

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
import vn.icommerce.icommerce.infra.rest.ApiResp;

/**
 * Handles forbidden request.
 *
 * <p>Created on 11/22/19.
 *
 * @author khoanguyenminh
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
