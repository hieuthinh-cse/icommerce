/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.sharedkernel.app.component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import vn.icommerce.sharedkernel.app.validator.QueryValidator;

/**
 * Constraint for search query validation.
 *
 * <p>Created on 12/23/19.
 *
 * @author tuanlethanh
 */
@Documented
@Constraint(validatedBy = QueryValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryConstraint {

  String message() default "{iam.query_constraint}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
