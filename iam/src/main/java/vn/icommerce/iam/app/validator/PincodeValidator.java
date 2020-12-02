package vn.icommerce.iam.app.validator;

import java.util.Objects;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PincodeValidator implements ConstraintValidator<PincodeConstraint, String> {

  private static final Pattern PINCODE_PATTERN = Pattern
      .compile("[0-9]{6}");


  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return Objects.nonNull(value) && PINCODE_PATTERN.matcher(value).matches();
  }
}
