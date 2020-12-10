

package vn.icommerce.common.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import vn.icommerce.common.elasticsearch.model.FieldId;
import vn.icommerce.common.elasticsearch.model.FieldState;
import vn.icommerce.common.elasticsearch.model.FieldType;
import vn.icommerce.common.elasticsearch.processor.SearchProcessor;
import vn.icommerce.common.elasticsearch.processor.SortProcessor;
import vn.icommerce.common.jackson.JacksonExecutor;

/**
 * Implementation that uses the Elasticsearch engine to perform the business.
 *
 *
 *
 *
 */
@Slf4j
@Component
public class ElasticsearchExecutor {

  private final RestHighLevelClient client;

  private final SearchProcessor searchProcessor;

  private final SortProcessor sortProcessor;

  private final JacksonExecutor jacksonExecutor;

  private final RetryTemplate retryTemplate;

  public ElasticsearchExecutor(
      RestHighLevelClient client,
      SearchProcessor searchProcessor,
      SortProcessor sortProcessor,
      JacksonExecutor jacksonExecutor,
      RetryTemplate retryTemplate) {
    this.client = client;
    this.searchProcessor = searchProcessor;
    this.sortProcessor = sortProcessor;
    this.jacksonExecutor = jacksonExecutor;
    this.retryTemplate = retryTemplate;
  }

  private FieldState toSearchFieldState(String index, String condition, FieldType fieldType) {
    var fieldValue = condition.substring(condition.indexOf(":") + 1).trim();

    if (condition.startsWith("-")) {
      return new FieldState()
          .setFieldId(new FieldId()
              .setIndex(index)
              .setFieldName(condition.substring(1, condition.indexOf(":")).trim())
              .setFieldType(fieldType))
          .setPositive(false)
          .setFieldValue(fieldValue);
    } else {
      return new FieldState()
          .setFieldId(new FieldId()
              .setIndex(index)
              .setFieldName(condition.substring(0, condition.indexOf(":")).trim())
              .setFieldType(fieldType))
          .setPositive(true)
          .setFieldValue(fieldValue);
    }
  }

  private void setQueryAndFilter(
      SearchSourceBuilder searchSourceBuilder,
      String index,
      List<String> query,
      List<String> filter) {
    var fieldStateList = new ArrayList<FieldState>();

    query
        .stream()
        .map(q -> toSearchFieldState(index, q, FieldType.TEXT))
        .forEach(fieldStateList::add);

    filter
        .stream()
        .map(f -> toSearchFieldState(index, f, FieldType.KEYWORD))
        .forEach(fieldStateList::add);

    searchSourceBuilder.query(searchProcessor.execute(fieldStateList));
  }

  private FieldState toSortFieldState(String condition) {
    if (condition.startsWith("-")) {
      return new FieldState()
          .setFieldId(new FieldId().setFieldName(condition.substring(1).trim()))
          .setPositive(false);
    } else {
      return new FieldState()
          .setFieldId(new FieldId().setFieldName(condition.trim()))
          .setPositive(true);
    }
  }

  private void setSort(SearchSourceBuilder searchSourceBuilder, List<String> sortFieldList) {
    sortFieldList
        .stream()
        .map(this::toSortFieldState)
        .collect(Collectors.collectingAndThen(
            Collectors.toList(),
            sortProcessor::execute))
        .forEach(searchSourceBuilder::sort);
  }

  private void setPaging(SearchSourceBuilder searchSourceBuilder, int page, int pageSize) {
    searchSourceBuilder
        .from(page * pageSize)
        .size(pageSize);
  }

  private void setIncludeFields(
      SearchSourceBuilder searchSourceBuilder,
      List<String> includeFields) {
    searchSourceBuilder.fetchSource(
        includeFields.toArray(new String[]{}),
        null);
  }

  private void setSearchAfter(SearchSourceBuilder searchSourceBuilder, List<Object> after) {
    if (Objects.nonNull(after) && !after.isEmpty()) {
      searchSourceBuilder.searchAfter(after.toArray());
    }
  }

  private SearchRequest buildSearchRequest(String index, List<String> query, List<String> filter,
      List<String> sort, int page, int pageSize, List<String> includeFields, List<Object> after) {
    var searchSourceBuilder = new SearchSourceBuilder()
        .trackTotalHitsUpTo(Integer.MAX_VALUE);

    setQueryAndFilter(searchSourceBuilder, index, query, filter);

    setSort(searchSourceBuilder, sort);

    setPaging(searchSourceBuilder, page, pageSize);

    setIncludeFields(searchSourceBuilder, includeFields);

    setSearchAfter(searchSourceBuilder, after);

    var searchRequest = new SearchRequest(index)
        .source(searchSourceBuilder)
        .searchType(SearchType.DFS_QUERY_THEN_FETCH);

    log.info(
        "method: buildSearchRequest, index: {} , query: {} , searchRequest: {}",
        index,
        query,
        searchRequest);

    return searchRequest;
  }

  public SearchResponse search(String index, List<String> query, List<String> filter,
      List<String> sort, int page, int pageSize, List<String> includeFields, List<Object> after) {
    try {
      var searchRequest = buildSearchRequest(index, query, filter, sort, page, pageSize,
          includeFields, after);

      return client.search(searchRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public GetResponse get(String index, String documentId, List<String> includeFields) {
    try {
      var fetchSourceContext = new FetchSourceContext(
          true,
          includeFields.toArray(new String[]{}),
          null
      );

      var getRequest = new GetRequest(index, documentId)
          .fetchSourceContext(fetchSourceContext);

      return client.get(getRequest, RequestOptions.DEFAULT);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void index(String indexName, String indexDocId, long version, Object data) {
    var indexRequest = new IndexRequest(indexName)
        .id(indexDocId)
        .source(jacksonExecutor.serializeAsBytes(data), XContentType.JSON)
        .version(version)
        .versionType(VersionType.EXTERNAL);

    retryTemplate.execute(context -> {
      try {
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        log.info("method: index, indexDocId: {} , version: {} , data: {} , indexResponse: {}",
            indexDocId, version, data, indexResponse);
      } catch (Exception e) {
        if (isIgnorableException(e)) {
          log.warn("method: index, indexDocId: {} , version: {} , data: {}", indexDocId, version,
              data);
        } else {
          throw new RuntimeException(e);
        }
      }

      return null;
    });
  }

  private boolean isIgnorableException(Exception e) {
    if (e instanceof ElasticsearchException) {
      RestStatus restStatus = ((ElasticsearchException) e).status();

      return restStatus == RestStatus.CONFLICT
          || restStatus == RestStatus.BAD_REQUEST;
    }

    return false;
  }
}
