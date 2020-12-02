package vn.icommerce.iam.infra.rest;

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
import vn.icommerce.iam.app.internal.InternalQueryBuyerAppService;
import vn.icommerce.sharedkernel.app.component.Query;

@Api(tags = "internal")
@RestController
public class InternalQueryAccountController {

  private final MessageSource messageSource;
  private final InternalQueryBuyerAppService internalQueryBuyerAppService;

  public InternalQueryAccountController(
      MessageSource messageSource,
      InternalQueryBuyerAppService internalQueryBuyerAppService) {
    this.messageSource = messageSource;
    this.internalQueryBuyerAppService = internalQueryBuyerAppService;
  }

  /**
   * Search the buyers matching the given query condition.
   *
   * @param query the query having info to search buyers
   * @return the API response.
   */
  @ApiOperation("Search buyers")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @GetMapping("/v1/internal/buyers")
  public ApiResp searchAccounts(@Validated Query query, Locale locale) {
    return new ApiResp()
        .setCode(REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(
            messageSource.getMessage(
                REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(), null, locale))
        .setData(internalQueryBuyerAppService.search(query));
  }

}
