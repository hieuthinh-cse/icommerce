

package vn.icommerce.common.elasticsearch.processor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.NestedSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;
import vn.icommerce.common.elasticsearch.model.FieldState;

/**
 * Implementation for sorting on a field.
 *
 *
 *
 *
 */
@Component
public class SortProcessor {

  private NestedSortBuilder getNestedSortBuilder(String[] fields, int index) {
    var path = new StringBuilder((fields[0]));
    for (int i = 0; i < index; i++) {
      path
          .append(".")
          .append(fields[i]);
    }

    var nestedSortBuilder = new NestedSortBuilder(path.toString());

    index++;

    if (index == fields.length - 1) {
      return nestedSortBuilder;
    } else {
      return nestedSortBuilder
          .setNestedSort(getNestedSortBuilder(fields, index));
    }
  }

  private FieldSortBuilder getFieldSortBuilder(FieldState fieldState) {
    var fieldSortBuilder = SortBuilders
        .fieldSort(fieldState.getFieldName());

    if (fieldState.isPositive()) {
      return fieldSortBuilder.order(SortOrder.ASC);
    } else {
      return fieldSortBuilder.order(SortOrder.DESC);
    }
  }

  private FieldSortBuilder getSortBuilder(FieldState fieldState) {
    var fieldSortBuilder = getFieldSortBuilder(fieldState);

    var fields = fieldState
        .getFieldName()
        .split("\\.");

    if (fields.length > 1) {
      return fieldSortBuilder.setNestedSort(getNestedSortBuilder(fields, 0));
    } else {
      return fieldSortBuilder;
    }
  }

  public List<SortBuilder> execute(Collection<FieldState> fieldStates) {
    if (fieldStates.isEmpty()) {
      return Collections.singletonList(SortBuilders.scoreSort());
    } else {
      return fieldStates
          .stream()
          .map(this::getSortBuilder)
          .collect(Collectors.toList());
    }
  }
}
