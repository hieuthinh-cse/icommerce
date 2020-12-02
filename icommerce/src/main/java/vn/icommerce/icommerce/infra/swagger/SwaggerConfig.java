package vn.icommerce.icommerce.infra.swagger;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static springfox.documentation.builders.PathSelectors.regex;

import com.google.common.base.Predicate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Config swagger
 *
 */
@Configuration
@EnableSwagger2
@Data
@ConfigurationProperties("icommerce.infra.swagger")
public class SwaggerConfig {

  /**
   * The base host to expose Swagger API.
   */
  private String host;

  /**
   * Configures a Swagger API.
   *
   * @return the configured Swagger API
   */
  @Bean
  public Docket api() {
    Parameter authorizationHeaderParam = new ParameterBuilder()
        .name("Authorization")
        .parameterType("header")
        .modelRef(new ModelRef("string"))
        .build();

    Parameter correlationIdHeaderParam = new ParameterBuilder()
        .name("Correlation-Id")
        .parameterType("header")
        .modelRef(new ModelRef("string"))
        .build();

    Parameter apiKeyHeaderParam = new ParameterBuilder()
        .name("Api-Key")
        .parameterType("header")
        .modelRef(new ModelRef("string"))
        .build();

    Parameter signatureHeaderParam = new ParameterBuilder()
        .name("Signature")
        .parameterType("header")
        .modelRef(new ModelRef("string"))
        .build();

    List<Parameter> headerParamList = Arrays.asList(
        authorizationHeaderParam,
        correlationIdHeaderParam,
        apiKeyHeaderParam,
        signatureHeaderParam);

    return new Docket(DocumentationType.SWAGGER_2)
        .host(host)
        .ignoredParameterTypes(Locale.class)
        .globalOperationParameters(headerParamList)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(excludePaths())
        .build()
        .apiInfo(apiEndPointsInfo())
        .useDefaultResponseMessages(false);
  }

  private ApiInfo apiEndPointsInfo() {
    return new ApiInfoBuilder().title("IAM REST API")
        .description("API Documentation")
        .version("1.0.0")
        .build();
  }

  @SuppressWarnings("Guava")
  private Predicate<String> excludePaths() {
    return and(
        not(regex("/error.*")),
        not(regex("/monitor.*")));
  }
}
