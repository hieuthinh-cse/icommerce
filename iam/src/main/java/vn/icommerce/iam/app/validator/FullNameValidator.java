package vn.icommerce.iam.app.validator;

import java.util.Objects;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FullNameValidator implements ConstraintValidator<FullNameConstraint, String> {

  private static final Pattern FULL_NAME_PATTERN = Pattern.compile("^[a-zA-Z ]+$");

  @Override
  public void initialize(FullNameConstraint constraintAnnotation) {

  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return Objects.isNull(value) || (!value.isBlank() && (FULL_NAME_PATTERN.matcher(value).matches()
        && has2Words(value)) && value.length()<= 100);
  }

  private boolean has2Words(String value) {
    String[] array = value.strip().split(" ");
    return array.length >= 2;
  }

}
