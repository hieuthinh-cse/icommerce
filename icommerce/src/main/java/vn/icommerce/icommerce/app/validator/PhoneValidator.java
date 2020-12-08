package vn.icommerce.icommerce.app.validator;

import java.util.Objects;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<PhoneConstraint, String> {

  // only support 10 digits phone number
  private static final Pattern PHONE_PATTERN = Pattern.compile("0[1-9][0-9]{8}");

  @Override
  public void initialize(PhoneConstraint constraintAnnotation) {

  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return Objects.isNull(value)
        || PHONE_PATTERN.matcher(value).matches();
  }
}
