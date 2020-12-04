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
 * This controller provides the {@code Product} manipulation API.
 */
@Api(tags = "internal products")
@RestController
public class InternalProductController {


  private final MessageSource messageSource;

  private final ProductAppService productAppService;


  /**
   * Constructor to inject dependent fields.
   */
  public InternalProductController(
      MessageSource messageSource,
      ProductAppService productAppService) {
    this.messageSource = messageSource;
    this.productAppService = productAppService;
  }

  /**
   * Create an unescrow transaction.
   *
   * @param cmd the command having info to create a product.
   * @return the API response.
   */
  @ApiOperation(value = "Create an product")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @PostMapping("/v1/internal/products")
  public ApiResp createProduct(@Valid @RequestBody CreateProductCmd cmd,
      Locale locale) {
    var productId = productAppService.createProduct(cmd);

    return new ApiResp()
        .setCode(DomainCode.REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(messageSource
            .getMessage(DomainCode.REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(), null, locale))
        .setData(ImmutableMap.of("productId", productId));
  }

}
