package vn.icommerce.iam.infra.rest;

import static vn.icommerce.sharedkernel.domain.model.DomainCode.REQUEST_PROCESSED_SUCCESSFULLY;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.icommerce.iam.app.buyer.BuyerAppService;
import vn.icommerce.iam.app.buyer.CreateBuyerCmd;
import vn.icommerce.iam.app.buyer.QueryBuyerAppService;
import vn.icommerce.iam.app.buyertoken.BuyerTokenAppService;
import vn.icommerce.iam.app.buyertoken.CreateBuyerTokenCmd;
import vn.icommerce.iam.app.buyertoken.CreateBuyerTokenWithPasswordCmd;

/**
 * This controller provides the {@code Account} manipulation API.
 *
 *
 */
@Api(tags = "buyer")
@RestController
@Validated
public class BuyerController {

  private final BuyerAppService buyerAppService;

  private final QueryBuyerAppService queryBuyerAppService;

  private final BuyerTokenAppService buyerTokenAppService;

  private final MessageSource messageSource;


  /**
   * Profile Controller constructor.
   *
   * @param buyerAppService      ...
   * @param queryBuyerAppService ...
   * @param buyerTokenAppService ...
   * @param messageSource        the one handles response message
   */
  public BuyerController(
      BuyerAppService buyerAppService,
      QueryBuyerAppService queryBuyerAppService,
      BuyerTokenAppService buyerTokenAppService,
      MessageSource messageSource
  ) {
    this.buyerAppService = buyerAppService;
    this.queryBuyerAppService = queryBuyerAppService;
    this.buyerTokenAppService = buyerTokenAppService;
    this.messageSource = messageSource;
  }

  /**
   * Creates a new buyer.
   *
   * @param cmd the command containing info to create a account
   * @return the API response.
   */
  @ApiOperation(value = "Creates a new buyer account")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @PostMapping("/v1/buyer")
  public ApiResp createAccount(
      @Valid @RequestBody CreateBuyerCmd cmd,
      Locale locale) {
    var accountId = buyerAppService.create(cmd);

    return new ApiResp()
        .setCode(REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(messageSource
            .getMessage(
                REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(),
                null,
                locale))
        .setData(Map.of("accountId", accountId));
  }

  @ApiOperation(value = "Generate a token")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @PostMapping("/v1/tokens")
  public ApiResp generate(@Valid @RequestBody CreateBuyerTokenCmd cmd, Locale locale) {

    var token = buyerTokenAppService.create(cmd);

    return new ApiResp()
        .setCode(REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(
            messageSource.getMessage(REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(), null, locale))
        .setData(Collections.singletonMap("token", token));
  }

  @ApiOperation(value = "Generate a token by password")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @PostMapping("/v1/tokens/by-password")
  public ApiResp generate(@Valid @RequestBody CreateBuyerTokenWithPasswordCmd cmd, Locale locale) {

    var token = buyerTokenAppService.createWithPassword(cmd);

    return new ApiResp()
        .setCode(REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(
            messageSource.getMessage(REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(), null, locale))
        .setData(Collections.singletonMap("token", token));
  }

  /**
   * View information of the buyer account.
   *
   * @return buyer info
   */
  @ApiOperation(value = "View buyer account information")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @GetMapping("/v1/me")
  public ApiResp viewAccount(@RequestParam(required = false) List<String> includeFields,
      Locale locale
  ) {
    includeFields = Objects.isNull(includeFields) ? Collections.emptyList() : includeFields;
    return new ApiResp()
        .setCode(REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(
            messageSource.getMessage(REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(), null, locale))
        .setData(queryBuyerAppService.get(includeFields));
  }
}
