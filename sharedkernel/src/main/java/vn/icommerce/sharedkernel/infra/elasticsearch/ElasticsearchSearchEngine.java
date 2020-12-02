/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.sharedkernel.infra.elasticsearch;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.icommerce.common.elasticsearch.ElasticsearchExecutor;
import vn.icommerce.sharedkernel.app.component.GetResult;
import vn.icommerce.sharedkernel.app.component.Query;
import vn.icommerce.sharedkernel.app.component.ResultStatus;
import vn.icommerce.sharedkernel.app.component.SearchEngine;
import vn.icommerce.sharedkernel.app.component.SearchResult;
import vn.icommerce.sharedkernel.app.component.SearchResult.SearchDataResult;

/**
 * Implementation that uses the Elasticsearch engine to perform the business.
 *
 */
@Component
public class ElasticsearchSearchEngine implements SearchEngine {

  private final ElasticsearchExecutor elasticsearchExecutor;

  private final int limitTotal;

  public ElasticsearchSearchEngine(
      ElasticsearchExecutor elasticsearchExecutor,
      @Value("#{elasticsearchConfig.limitTotal}") int limitTotal) {
    this.elasticsearchExecutor = elasticsearchExecutor;
    this.limitTotal = limitTotal;
  }

  private SearchResult toSearchResult(SearchResponse searchResponse, int page, int pageSize) {
    if (RestStatus.OK == searchResponse.status()) {
      var records = Stream
          .of(searchResponse.getHits().getHits())
          .map(SearchHit::getSourceAsMap)
          .collect(Collectors.toList());

      var searchDataResult = new SearchDataResult()
          .setRecords(records)
          .setPage(page)
          .setPageSize(pageSize)
          .setTotal(searchResponse.getHits().getTotalHits().value)
          .setLimitTotal(limitTotal);

      return new SearchResult()
          .setResultStatus(ResultStatus.OK)
          .setMessage(ResultStatus.OK.name())
          .setSearchDataResult(searchDataResult);
    }

    return new SearchResult()
        .setResultStatus(ResultStatus.FAILED)
        .setMessage(searchResponse.toString());
  }

  @Override
  public SearchResult search(String index, Query query) {
    var searchResponse = elasticsearchExecutor.search(
        index,
        query.getQuery(),
        query.getFilter(),
        query.getSort(),
        query.getPage(),
        query.getPageSize(),
        query.getIncludeFields(),
        query.getAfter());

    return toSearchResult(searchResponse, query.getPage(), query.getPageSize());
  }

  @Override
  public GetResult get(String index, String documentId, List<String> includeFields) {
    var getResponse = elasticsearchExecutor.get(
        index,
        documentId,
        includeFields);

    return toGetResult(getResponse);
  }

  private GetResult toGetResult(GetResponse getResponse) {
    if (getResponse.isExists()) {
      return new GetResult()
          .setResultStatus(ResultStatus.OK)
          .setMessage(ResultStatus.OK.name())
          .setValue(getResponse.getSourceAsMap());
    }

    return new GetResult()
        .setResultStatus(ResultStatus.FAILED)
        .setMessage(getResponse.toString());
  }

  @Override
  public void index(String indexName, String indexDocId, long version, Object data) {
    elasticsearchExecutor.index(indexName, indexDocId, version, data);
  }
}
