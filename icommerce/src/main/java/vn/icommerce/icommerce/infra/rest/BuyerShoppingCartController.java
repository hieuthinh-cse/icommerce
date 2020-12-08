package vn.icommerce.icommerce.infra.rest;

import static vn.icommerce.sharedkernel.domain.model.DomainCode.REQUEST_PROCESSED_SUCCESSFULLY;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Locale;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.icommerce.icommerce.app.cart.AddProduct2CartCmd;
import vn.icommerce.icommerce.app.cart.BuyerShoppingCartAppService;
import vn.icommerce.icommerce.app.cart.QueryShoppingCartAppService;
import vn.icommerce.icommerce.app.cart.UpdateProductCartCmd;
import vn.icommerce.sharedkernel.app.component.Query;
import vn.icommerce.sharedkernel.domain.model.DomainCode;


@Api(tags = "buyer shopping cart controller")
@RestController
public class BuyerShoppingCartController {


  private final MessageSource messageSource;

  private final BuyerShoppingCartAppService buyerShoppingCartAppService;

  private final QueryShoppingCartAppService queryShoppingCartAppService;


  /**
   * Constructor to inject dependent fields.
   */
  public BuyerShoppingCartController(
      MessageSource messageSource,
      BuyerShoppingCartAppService buyerShoppingCartAppService,
      QueryShoppingCartAppService queryShoppingCartAppService) {
    this.messageSource = messageSource;
    this.buyerShoppingCartAppService = buyerShoppingCartAppService;
    this.queryShoppingCartAppService = queryShoppingCartAppService;
  }

  /**
   * Search the requests matching the given query condition.
   *
   * @param query the query having info to search the logs
   * @return the API response.
   */
  @ApiOperation(value = "Search shopping cart")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @GetMapping("/v1/me/shopping-cart")
  public ApiResp searchRequests(@Validated Query query, Locale locale) {
    return new ApiResp()
        .setCode(REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(messageSource.getMessage(
            REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(), null, locale))
        .setData(queryShoppingCartAppService.get(query));
  }

  @ApiOperation(value = "Update a product in cart")
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

  @ApiOperation(value = "Add a product to cart")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @PutMapping("/v1/me/carts/items")
  public ApiResp addProductCart(@Valid @RequestBody AddProduct2CartCmd cmd,
      Locale locale) {
    buyerShoppingCartAppService.addProductCart(cmd);

    return new ApiResp()
        .setCode(DomainCode.REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(messageSource
            .getMessage(DomainCode.REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(), null, locale));
  }

}
