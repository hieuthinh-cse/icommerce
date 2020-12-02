package vn.icommerce.dbtool.cmd;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.elasticsearch.client.RestHighLevelClient;

public abstract class AbstractElasticsearchCmd implements Cmd {

  protected final RestHighLevelClient client;

  protected final Set<String> optionSet;

  public AbstractElasticsearchCmd(RestHighLevelClient client) {
    this.client = client;
    this.optionSet = new HashSet<>();
  }

  @Override
  public boolean addOptionValue(String option, List<String> optionValueList) {
    if ("index".equals(option)) {
      optionSet.addAll(optionValueList);
      return true;
    }

    return false;
  }
}
