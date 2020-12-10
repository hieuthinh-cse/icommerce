package vn.icommerce.icommerce.infra.rest;

import static vn.icommerce.sharedkernel.domain.model.DomainCode.REQUEST_PROCESSED_SUCCESSFULLY;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.icommerce.icommerce.app.order.BuyerOrderAppService;
import vn.icommerce.icommerce.app.order.BuyerQueryOrderAppService;
import vn.icommerce.icommerce.app.order.CreateOrderCmd;
import vn.icommerce.sharedkernel.app.component.Query;

@Api(tags = "buyer order controller")
@RestController
public class BuyerOrderController {

  private final MessageSource messageSource;

  private final BuyerOrderAppService buyerOrderAppService;

  private final BuyerQueryOrderAppService buyerQueryOrderAppService;

  public BuyerOrderController(
      MessageSource messageSource,
      BuyerOrderAppService buyerShoppingCartAppService1,
      BuyerQueryOrderAppService buyerQueryOrderAppService) {
    this.messageSource = messageSource;
    this.buyerOrderAppService = buyerShoppingCartAppService1;
    this.buyerQueryOrderAppService = buyerQueryOrderAppService;
  }

  @ApiOperation(value = "Create an order")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @PostMapping("/v1/me/orders")
  public ApiResp createOrder(@Valid @RequestBody CreateOrderCmd cmd, Locale locale) {
    var response = buyerOrderAppService.createOrder(cmd);

    return new ApiResp()
        .setCode(response.getDomainCode().value())
        .setMessage(messageSource
            .getMessage(response.getDomainCode().valueAsString(), null, locale));
  }

  @ApiOperation(value = "Get order")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @GetMapping("/v1/me/orders/{orderId}")
  public ApiResp searchRequests(@PathVariable Long orderId,
      @RequestParam(required = false) List<String> includeFields, Locale locale) {

    includeFields = Objects.isNull(includeFields) ? Collections.emptyList() : includeFields;

    return new ApiResp()
        .setCode(REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(
            messageSource.getMessage(REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(), null, locale))
        .setData(buyerQueryOrderAppService.get(orderId, includeFields));
  }

  @ApiOperation("Search orders")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @GetMapping("/v1/me/orders")
  public ApiResp searchAccounts(@Validated Query query, Locale locale) {
    return new ApiResp()
        .setCode(REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(
            messageSource.getMessage(
                REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(), null, locale))
        .setData(buyerQueryOrderAppService.search(query));
  }
}
