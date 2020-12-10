package vn.icommerce.icommerce.infra.rest;

import junitparams.JUnitParamsRunner;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import vn.icommerce.common.jackson.JacksonExecutor;
import vn.icommerce.common.jackson.JacksonMarker;
import vn.icommerce.icommerce.app.cart.BuyerShoppingCartAppService;
import vn.icommerce.icommerce.app.cart.QueryShoppingCartAppService;
import vn.icommerce.icommerce.app.order.BuyerOrderAppService;
import vn.icommerce.icommerce.app.order.BuyerQueryOrderAppService;
import vn.icommerce.icommerce.app.product.ProductAppService;
import vn.icommerce.icommerce.app.product.QueryProductAppService;
import vn.icommerce.icommerce.infra.springsecurity.SecurityConfig;

@RunWith(JUnitParamsRunner.class)
@WebMvcTest
@ContextConfiguration
@MockBean(JpaMetamodelMappingContext.class)
public abstract class WebMvcIntegrationTest extends AbstractIntegrationTest {

  @TestConfiguration
  @ComponentScan(basePackageClasses = {
      ApiRespMsgConfig.class,
      SecurityConfig.class,
      JacksonMarker.class
  })
  public static class TestConfig {

  }

  @ClassRule
  public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

  @Rule
  public final SpringMethodRule springMethodRule = new SpringMethodRule();

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected JacksonExecutor jacksonExecutor;

  @MockBean
  protected ProductAppService productAppService;

  @MockBean
  protected QueryProductAppService queryProductAppService;

  @MockBean
  protected BuyerOrderAppService buyerOrderAppService;

  @MockBean
  protected BuyerQueryOrderAppService buyerQueryOrderAppService;

  @MockBean
  protected BuyerShoppingCartAppService buyerShoppingCartAppService;

  @MockBean
  protected QueryShoppingCartAppService queryShoppingCartAppService;

}
