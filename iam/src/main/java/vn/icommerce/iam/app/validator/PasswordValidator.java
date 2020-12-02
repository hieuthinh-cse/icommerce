/*
 * Copyright 2020 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.iam.app.validator;

import java.util.Objects;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <p>Created on 06/22/2020.
 *
 * @author tuanlt2
 */
public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {

  // at least one [A-Z]
  // at least one [a-z]
  // at least one [0-9]
  // at least one ~`!@#$%^&*()-_+={}[]|\;:"<>,./?
  // length from 8 to 32
  private static final Pattern PASSWORD_PATTERN = Pattern
      .compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~`!@#$%^&*()\\-_+={}\\[\\]|\\\\;:\"<>,./?]).{8,32}$");

  @Override
  public void initialize(PasswordConstraint constraintAnnotation) {

  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return Objects.isNull(value)
        || PASSWORD_PATTERN.matcher(value).matches();
  }
}
