/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.infra.rest;

import static vn.icommerce.sharedkernel.domain.model.DomainCode.REQUEST_PROCESSED_SUCCESSFULLY;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.icommerce.icommerce.app.product.QueryProductAppService;
import vn.icommerce.sharedkernel.app.component.Query;

/**
 * This controller provides the {@code Request} manipulation API.
 *
 * <p>Created on 10/31/19.
 *
 * @author thinh.nguyenhieu
 */
@Api(tags = "operator request")
@RestController
public class OptQueryRequestController {

  private final QueryProductAppService queryProductAppService;

  private final MessageSource messageSource;

  /**
   * Constructor to inject dependent fields.
   */
  public OptQueryRequestController(
      QueryProductAppService queryProductAppService,
      MessageSource messageSource
  ) {
    this.queryProductAppService = queryProductAppService;
    this.messageSource = messageSource;
  }

  /**
   * Search the requests matching the given query condition.
   *
   * @param query the query having info to search the logs
   * @return the API response.
   */
  @ApiOperation(value = "Search requests")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @GetMapping("/v1/opt/products")
  public ApiResp searchRequests(@Validated Query query, Locale locale) {
    return new ApiResp()
        .setCode(REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(messageSource.getMessage(
            REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(), null, locale))
        .setData(queryProductAppService.search(query));
  }
}
