package vn.icommerce.dbtool.cmd;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import vn.icommerce.common.jackson.JacksonExecutor;

@Component
public class ElasticsearchMigrateCmd extends AbstractElasticsearchCmd {

  private final JacksonExecutor jacksonExecutor;

  private final ResourcePatternResolver resolver;

  public ElasticsearchMigrateCmd(
      RestHighLevelClient client,
      JacksonExecutor jacksonExecutor,
      ResourcePatternResolver resolver) {
    super(client);
    this.jacksonExecutor = jacksonExecutor;
    this.resolver = resolver;
  }

  private File toInputFile(Resource resource) {
    try {
      return resource.getFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private EsSchema toEsSchema(File inputFile) {
    return new EsSchema()
        .setIndexName(inputFile.getName().replace(".json", ""))
        .setSchema(jacksonExecutor.deserializeFromFile(
            inputFile,
            new TypeReference<>() {
            }));
  }

  private CreateIndexRequest toEsCreateIndexRequest(EsSchema esSchema) {
    return new CreateIndexRequest(esSchema.getIndexName())
        .source(esSchema.getSchema());
  }

  private void createEsIndex(CreateIndexRequest createIndexRequest) {
    try {
      var createIndexResponse = client
          .indices()
          .create(createIndexRequest, RequestOptions.DEFAULT);

      if (!createIndexResponse.isAcknowledged()) {
        throw new RuntimeException(
            "Failed to create the new index from json because the request is unacknowledged");
      }
    } catch (IOException e) {
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

  private Resource getResource(String indexName) {
    try {
      return resolver.getResource(String.format(
          "file:%s/../elasticsearch/%s.json",
          System.getProperty("user.dir"),
          indexName));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public void run() {
    if (optionSet.contains("*")) {
      Stream
          .of(getResources())
          .map(this::toInputFile)
          .map(this::toEsSchema)
          .map(this::toEsCreateIndexRequest)
          .forEach(this::createEsIndex);
    } else {
      optionSet
          .stream()
          .map(this::getResource)
          .map(this::toInputFile)
          .map(this::toEsSchema)
          .map(this::toEsCreateIndexRequest)
          .forEach(this::createEsIndex);
    }
  }

  @Accessors(chain = true)
  @Setter
  @Getter
  private static class EsSchema {

    private String indexName;

    private Map<String, Object> schema;
  }
}
