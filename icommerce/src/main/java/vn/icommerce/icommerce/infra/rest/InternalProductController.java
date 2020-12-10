package vn.icommerce.icommerce.infra.rest;

import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Locale;
import javax.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.icommerce.icommerce.app.product.CreateProductCmd;
import vn.icommerce.icommerce.app.product.ProductAppService;
import vn.icommerce.icommerce.app.product.UpdateProductCmd;
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

  @ApiOperation(value = "Update an product")
  @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ApiResp.class))
  @PutMapping("/v1/internal/products/{productId}")
  public ApiResp updateProduct(@PathVariable Long productId,
      @Valid @RequestBody UpdateProductCmd cmd,
      Locale locale) {
    cmd.setProductId(productId);
    productAppService.updateProduct(cmd);

    return new ApiResp()
        .setCode(DomainCode.REQUEST_PROCESSED_SUCCESSFULLY.value())
        .setMessage(messageSource
            .getMessage(DomainCode.REQUEST_PROCESSED_SUCCESSFULLY.valueAsString(), null, locale))
        .setData(null);
  }

}
