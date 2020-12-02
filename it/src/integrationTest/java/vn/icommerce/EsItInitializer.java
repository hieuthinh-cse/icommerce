package vn.icommerce;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import vn.icommerce.common.jackson.JacksonConfig;

public class EsItInitializer extends AbstractItInitializer {

  private static ElasticsearchContainer elasticsearchContainer;

  static {
    if (PIPELINE_ENV) {
      startEsContainer();
      initDataForEsContainer();
    } else {
      initDataForLocalDev();
    }
  }

  private static void startEsContainer() {
    elasticsearchContainer = new ElasticsearchContainer("elasticsearch:7.4.2");
    elasticsearchContainer.start();
  }

  private static void initDataForEsContainer() {
    try {
      var mapper = new JacksonConfig().objectMapper();

      var client = new RestHighLevelClient(
          RestClient.builder(HttpHost.create(elasticsearchContainer.getHttpHostAddress())));

      var resolver = new PathMatchingResourcePatternResolver();

      initSchema(mapper, client, resolver);
      initData(mapper, client, resolver);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static void initSchema(
      ObjectMapper mapper,
      RestHighLevelClient client,
      ResourcePatternResolver resolver) throws Exception {
    var resources = resolver.getResources(String.format(
        "file:%s/../elasticsearch/*.json",
        System.getProperty("user.dir")));

    Stream.of(resources)
        .map(EsItInitializer::toInputFile)
        .map(inputFile -> toEsSchema(mapper, inputFile))
        .map(EsItInitializer::toEsCreateIndexRequest)
        .forEach(createIndexRequest -> {
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
        });
  }


  private static CreateIndexRequest toEsCreateIndexRequest(EsSchema esSchema) {
    return new CreateIndexRequest(esSchema.indexName)
        .source(esSchema.schema);
  }

  private static EsSchema toEsSchema(ObjectMapper objectMapper, File inputFile) {
    try {
      var schema = objectMapper.readValue(
          inputFile,
          new TypeReference<Map<String, Object>>() {
          });

      return new EsSchema()
          .setIndexName(inputFile.getName().replace(".json", ""))
          .setSchema(schema);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void initData(
      ObjectMapper mapper,
      RestHighLevelClient client,
      ResourcePatternResolver resolver) throws Exception {
    var bulkRequest = new BulkRequest()
        .setRefreshPolicy(RefreshPolicy.IMMEDIATE);

    Stream.of(resolver.getResources("classpath:elasticsearch/*.json"))
        .map(EsItInitializer::toInputFile)
        .map(inputStream -> toEsIndex(mapper, inputStream))
        .map(EsItInitializer::toIndexRequestList)
        .flatMap(Collection::stream)
        .forEach(bulkRequest::add);

    var bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

    if (bulkResponse.hasFailures()) {
      throw new RuntimeException(
          String.format("Failed to index data. Reason: %s", bulkResponse.buildFailureMessage()));
    }
  }

  private static void initDataForLocalDev() {
    try {
      var mapper = new JacksonConfig().objectMapper();

      var client = new RestHighLevelClient(
          RestClient.builder(HttpHost.create("http://localhost:9200")));

      var resolver = new PathMatchingResourcePatternResolver();

      initData(mapper, client, resolver);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static List<IndexRequest> toIndexRequestList(EsIndex esIndex) {
    return esIndex
        .records
        .stream()
        .map(record -> new IndexRequest(esIndex.indexName)
            .id(record.get(esIndex.indexIdName).toString())
            .source(record))
        .collect(Collectors.toList());
  }

  private static EsIndex toEsIndex(ObjectMapper objectMapper, File inputFile) {
    try {
      return objectMapper.readValue(inputFile, EsIndex.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static File toInputFile(Resource resource) {
    try {
      return resource.getFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Accessors(chain = true)
  @Setter
  public static class EsIndex {

    private String indexName;

    private String indexIdName;

    private List<Map<String, Object>> records;
  }

  @Accessors(chain = true)
  @Setter
  public static class EsSchema {

    private String indexName;

    private Map<String, Object> schema;
  }

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    Optional
        .ofNullable(elasticsearchContainer)
        .ifPresent(container -> useContainerForEsTest(applicationContext));
  }

  private void useContainerForEsTest(ConfigurableApplicationContext applicationContext) {
    TestPropertyValues
        .of(
            "common.infra.elasticsearch.hosts=" + "http://" + elasticsearchContainer
                .getHttpHostAddress())
        .applyTo(applicationContext.getEnvironment());
  }
}
