package vn.icommerce.iam.app.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Constraint for phone validation.
 *
 * <p>Created on 8/27/19.
 *
 * @author hungphamchanh
 */
@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneConstraint {

  String message() default "{iam.phone_constraint}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
