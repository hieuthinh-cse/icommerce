

package vn.icommerce.dbtool.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import vn.icommerce.common.jackson.JacksonExecutor;
import vn.icommerce.common.springtx.SpringTxExecutor;

@Slf4j
public abstract class AbstractEsImportService<T, IdT, IdViewT> implements EsImportService {

  private static final int BATCH_SIZE = 500;

  private final SpringTxExecutor springTxExecutor;

  protected final RestHighLevelClient client;

  protected final JacksonExecutor jacksonExecutor;

  public AbstractEsImportService(
      SpringTxExecutor springTxExecutor,
      RestHighLevelClient client,
      JacksonExecutor jacksonExecutor) {
    this.springTxExecutor = springTxExecutor;
    this.jacksonExecutor = jacksonExecutor;
    this.client = client;
  }

  protected abstract Slice<IdViewT> findById(Pageable pageable);

  protected abstract IdT transform(IdViewT view);

  protected abstract List<T> findByIdIn(List<IdT> idList);

  protected abstract String getIndexName();

  protected abstract IndexRequest buildIndexRequest(T record);

  @Override
  public void importFromDb() {
    var pageWrapper = new PageWrapper<IdViewT>();

    do {
      springTxExecutor.doInReadOnlyTx(() -> {
        pageWrapper.recordPage = findById(PageRequest.of(
            pageWrapper.pageCount++, BATCH_SIZE,
            Sort.by(Order.asc("createdAt"))));

        var idList = pageWrapper
            .recordPage
            .getContent()
            .stream()
            .map(this::transform)
            .collect(Collectors.toList());

        if (!idList.isEmpty()) {
          var records = findByIdIn(idList);

          var bulkRequest = new BulkRequest(getIndexName())
              .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

          records.forEach(record -> bulkRequest.add(buildIndexRequest(record)));

          try {
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info("Index {} at page: {}, size: {}", getIndexName(), pageWrapper.pageCount,
                records.size());
          } catch (IOException e) {
            log.warn("Index {} at page: {}, size: {}", getIndexName(), pageWrapper.pageCount,
                records.size(), e);
          }
        }
      });
    } while (pageWrapper.recordPage.hasNext());
  }

  private static class PageWrapper<IdViewT> {

    private int pageCount = 0;

    private Slice<IdViewT> recordPage;
  }
}
