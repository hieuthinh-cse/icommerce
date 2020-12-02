package vn.icommerce.dbtool.cmd;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.stereotype.Component;

@Component
public class ElasticsearchCleanCmd extends AbstractElasticsearchCmd {

  public ElasticsearchCleanCmd(RestHighLevelClient client) {
    super(client);
  }

  private boolean indexExists(String indexName) {
    try {
      return client
          .indices()
          .exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void deleteIndex(String indexName) {
    try {
      var acknowledgedResponse = client
          .indices()
          .delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT);

      if (!acknowledgedResponse.isAcknowledged()) {
        throw new RuntimeException(
            "Failed to delete the existing index since the request is unacknowledged");
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void run() {
    if (optionSet.contains("*")) {
      deleteIndex("*");
    } else {
      optionSet
          .stream()
          .filter(this::indexExists)
          .forEach(this::deleteIndex);
    }
  }
}
