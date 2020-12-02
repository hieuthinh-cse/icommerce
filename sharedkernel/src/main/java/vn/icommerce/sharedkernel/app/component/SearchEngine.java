/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.sharedkernel.app.component;

import java.util.List;

/**
 * Interface to the query engine.
 *
 * <p>Created on 9/21/19.
 *
 * @author khoanguyenminh
 */
public interface SearchEngine {

  /**
   * Searches the documents matching the query condition.
   *
   * @param indexName the indexName to search against
   * @param query     the query condition to match
   * @return the search result
   */
  SearchResult search(String indexName, Query query);

  /**
   * Gets the document matching the given id.
   *
   * @param indexName  the indexName to search against
   * @param documentId the id of the document
   * @return the get result
   */
  GetResult get(String indexName, String documentId, List<String> includeFields);

  /**
   * Index the given document into Elasticsearch.
   *
   * @param indexName  the name of the index
   * @param indexDocId the document id
   * @param version    the document version
   * @param data       the document data
   */
  void index(String indexName, String indexDocId, long version, Object data);
}
