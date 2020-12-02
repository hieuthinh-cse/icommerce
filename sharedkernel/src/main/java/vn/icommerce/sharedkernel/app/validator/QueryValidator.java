package vn.icommerce.sharedkernel.app.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import vn.icommerce.sharedkernel.app.component.Query;
import vn.icommerce.sharedkernel.app.component.QueryConstraint;

/**
 * Validator for search query.
 *
 * <p>Created on 12/23/19.
 *
 * @author tuanlethanh
 */
public class QueryValidator implements ConstraintValidator<QueryConstraint, Query> {

  @Override
  public void initialize(QueryConstraint constraintAnnotation) {

  }

  @Override
  public boolean isValid(Query query, ConstraintValidatorContext context) {
    return ((query.getPage() + 1) * query.getPageSize()) <= 10000;
  }
}
