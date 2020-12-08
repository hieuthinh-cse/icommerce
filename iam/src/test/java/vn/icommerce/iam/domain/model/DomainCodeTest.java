package vn.icommerce.iam.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.MessageSource;
import vn.icommerce.iam.infra.rest.ApiRespMsgConfig;
import vn.icommerce.sharedkernel.domain.model.DomainCode;

@RunWith(JUnit4.class)
public class DomainCodeTest {

  private List<String> getDomainCodes() {
    return Stream
        .of(DomainCode.values())
        .map(DomainCode::valueAsString)
        .collect(Collectors.toList());
  }

  @Test
  public void givenNotDuplicateDomainCode_WhenInitDomainCode_ThenReturnOk() {
    assertThat(getDomainCodes()).doesNotHaveDuplicates();
  }

  @Test
  public void givenDomainCode_WhenInitDomainCode_ThenReturnHasMessageProperties() {
    MessageSource messageSource = new ApiRespMsgConfig().messageSource();

    getDomainCodes()
        .forEach(code -> assertThatCode(() -> messageSource.getMessage(code, null, null))
            .doesNotThrowAnyException());
  }
}
