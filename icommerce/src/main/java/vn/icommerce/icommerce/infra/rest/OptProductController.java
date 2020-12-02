/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.infra.rest;

import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Locale;
import javax.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.icommerce.icommerce.app.product.CreateProductCmd;
import vn.icommerce.icommerce.app.product.ProductAppService;
import vn.icommerce.sharedkernel.domain.model.DomainCode;

/**
 * This controller provides the {@code Transaction} manipulation API.
 *
 * <p>Created on 8/20/19.
 *
 * @author khoanguyenminh
 */
@Api(tags = "operator transactions")
@RestController
public class OptProductController {


  private final MessageSource messageSource;

  private final ProductAppService productAppService;


  /**
   * Constructor to inject dependent fields.
   */
  public OptProductController(
      MessageSource messageSource,
      ProductAppService productAppService) {
    this.messageSource = messageSource;
    this.productAppService = productAppService;
  }

  /**
   * Create an unescrow transaction.
   *
   * @param cmd the command having info to create transaction.
   * @return the API response.
   */
  @ApiOperation(value = "Create an escrow transaction")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @PostMapping("/v1/opt/products")
  public ApiResp cancelEscrowTransaction(@Valid @RequestBody CreateProductCmd cmd,
      Locale locale) {
    var productId = productAppService.createProduct(cmd);

    return new ApiResp()
        .setCode(DomainCode.REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(messageSource
            .getMessage(DomainCode.REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(), null, locale))
        .setData(ImmutableMap.of("productId", productId));
  }

}
