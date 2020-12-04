/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.icommerce.infra.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Locale;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.icommerce.icommerce.app.cart.AddProduct2CartCmd;
import vn.icommerce.icommerce.app.cart.BuyerShoppingCartAppService;
import vn.icommerce.icommerce.app.cart.UpdateProductCartCmd;
import vn.icommerce.sharedkernel.domain.model.DomainCode;

/**
 * This controller provides the {@code Transaction} manipulation API.
 *
 * <p>Created on 8/20/19.
 *
 * @author khoanguyenminh
 */
@Api(tags = "buyer shopping cart controller")
@RestController
public class BuyerShoppingCartController {


  private final MessageSource messageSource;

  private final BuyerShoppingCartAppService buyerShoppingCartAppService;


  /**
   * Constructor to inject dependent fields.
   */
  public BuyerShoppingCartController(
      MessageSource messageSource,
      BuyerShoppingCartAppService buyerShoppingCartAppService) {
    this.messageSource = messageSource;
    this.buyerShoppingCartAppService = buyerShoppingCartAppService;
  }

  /**
   * Create an unescrow transaction.
   *
   * @param cmd the command having info to create transaction.
   * @return the API response.
   */
  @ApiOperation(value = "Create an escrow transaction")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @PostMapping("/v1/me/carts/items/{itemId}")
  public ApiResp updateProductCart(@Valid @RequestBody UpdateProductCartCmd cmd,
      @PathVariable UUID itemId, Locale locale) {

    cmd.setCartItemId(itemId);
    buyerShoppingCartAppService.updateProductCart(cmd);

    return new ApiResp()
        .setCode(DomainCode.REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(messageSource
            .getMessage(DomainCode.REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(), null, locale));
  }

  /**
   * Create an unescrow transaction.
   *
   * @param cmd the command having info to create transaction.
   * @return the API response.
   */
  @ApiOperation(value = "Create an escrow transaction")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @PostMapping("/v1/me/carts/items")
  public ApiResp addProductCart(@Valid @RequestBody AddProduct2CartCmd cmd,
      Locale locale) {
    buyerShoppingCartAppService.addProductCart(cmd);

    return new ApiResp()
        .setCode(DomainCode.REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(messageSource
            .getMessage(DomainCode.REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(), null, locale));
  }

}
