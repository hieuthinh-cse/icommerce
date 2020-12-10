package vn.icommerce.icommerce.infra.rest;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.springframework.http.MediaType;
import vn.icommerce.sharedkernel.app.component.SearchResult.SearchDataResult;


public class BuyerQueryProductControllerTest extends WebMvcIntegrationTest {

  private String testWalletId = "777130919990";

  private SearchDataResult createSearchResult() {
    Map<String, Object> record = new HashMap<>();
    record.put("productId", testWalletId);
    return new SearchDataResult()
        .setPage(1)
        .setRecords(Collections.singletonList(record));
  }

  @Test
  public void givenCorrectInput_WhenQueryProduct_ThenReturn200_000() throws Exception {
    var searchDataResult = createSearchResult();
    given(queryProductAppService.search(any()))
        .willReturn(searchDataResult);
    mockMvc
        .perform(get("/v1/products")
            .param("filter", "productId:777130919990")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message", is("Yêu cầu thực hiện thành công")))
        .andExpect(jsonPath("$.data.records").isArray())
        .andExpect(jsonPath("$.data.records[0].productId", is(testWalletId)));
  }
}
