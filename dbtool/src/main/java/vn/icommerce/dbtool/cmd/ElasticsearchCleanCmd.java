package vn.icommerce.dbtool.cmd;

import java.util.stream.Stream;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

@Component
public class ElasticsearchCleanCmd extends AbstractElasticsearchCmd {

  private final ResourcePatternResolver resolver;

  public ElasticsearchCleanCmd(RestHighLevelClient client,
      ResourcePatternResolver resolver) {
    super(client);
    this.resolver = resolver;
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

  private Resource[] getResources() {
    try {
      return resolver.getResources(String.format(
          "file:%s/../elasticsearch/*.json",
          System.getProperty("user.dir")));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String toIndexName(Resource resource) {
    return resource.getFilename().replace(".json", "");
  }

  @Override
  public void run() {
    if (optionSet.contains("*")) {
      Stream
          .of(getResources())
          .map(this::toIndexName)
          .filter(this::indexExists)
          .forEach(this::deleteIndex);
    } else {
      optionSet
          .stream()
          .filter(this::indexExists)
          .forEach(this::deleteIndex);
    }
  }
}
