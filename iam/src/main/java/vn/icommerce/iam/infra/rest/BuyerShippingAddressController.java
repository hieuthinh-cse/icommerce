package vn.icommerce.iam.infra.rest;

import static vn.icommerce.sharedkernel.domain.model.DomainCode.REQUEST_PROCESSED_SUCCESSFULLY;

import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Locale;
import javax.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.icommerce.iam.app.address.BuyerAddressAppService;
import vn.icommerce.iam.app.address.BuyerQueryAddressAppService;
import vn.icommerce.iam.app.address.CreateAddressCmd;
import vn.icommerce.sharedkernel.app.component.Query;
import vn.icommerce.sharedkernel.domain.model.DomainCode;

@Api(tags = "buyer shipping controller")
@RestController
public class BuyerShippingAddressController {

  private final MessageSource messageSource;

  private final BuyerAddressAppService buyerAddressAppService;

  private final BuyerQueryAddressAppService buyerQueryAddressAppService;

  public BuyerShippingAddressController(
      MessageSource messageSource,
      BuyerAddressAppService buyerAddressAppService,
      BuyerQueryAddressAppService buyerQueryAddressAppService) {
    this.messageSource = messageSource;
    this.buyerAddressAppService = buyerAddressAppService;
    this.buyerQueryAddressAppService = buyerQueryAddressAppService;
  }

  @ApiOperation(value = "Create an address")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @PostMapping("/v1/me/shipping-addresses")
  public ApiResp updateProductCart(@Valid @RequestBody CreateAddressCmd cmd, Locale locale) {
    var addressId = buyerAddressAppService.createAddress(cmd);

    return new ApiResp()
        .setCode(DomainCode.REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(messageSource
            .getMessage(DomainCode.REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(), null, locale))
        .setData(ImmutableMap.of("addressId", addressId));
  }

  @ApiOperation(value = "Query address")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @GetMapping("/v1/me/shipping-addresses")
  public ApiResp searchRequests(@Validated Query query, Locale locale) {

    return new ApiResp()
        .setCode(REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(messageSource.getMessage(
            REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(), null, locale))
        .setData(buyerQueryAddressAppService.search(query));
  }
}
