package vn.icommerce.icommerce.infra.rest;

import static vn.icommerce.sharedkernel.domain.model.DomainCode.ARGUMENT_NOT_VALID;
import static vn.icommerce.sharedkernel.domain.model.DomainCode.HTTP_MEDIA_TYPE_NOT_SUPPORTED;
import static vn.icommerce.sharedkernel.domain.model.DomainCode.HTTP_MESSAGE_NOT_READABLE;
import static vn.icommerce.sharedkernel.domain.model.DomainCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED;
import static vn.icommerce.sharedkernel.domain.model.DomainCode.METHOD_ARGUMENT_TYPE_MISMATCH;
import static vn.icommerce.sharedkernel.domain.model.DomainCode.MISSING_REQUEST_HEADER;
import static vn.icommerce.sharedkernel.domain.model.DomainCode.MISSING_SERVLET_REQUEST_PARAMETER;
import static vn.icommerce.sharedkernel.domain.model.DomainCode.UNKNOWN_ERROR;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import vn.icommerce.sharedkernel.domain.exception.DomainException;

/**
 * Once defining a specific exception to be caught in this global exception handler, it will apply
 * to exception thrown from <b>any</b> controller.
 *
 * <p><b>Note:</b>
 * <ul>
 * <li>{@link ExceptionHandler @ExceptionHandler} methods on the {@code Controller} are always
 * selected before those on this handler.
 *  <li>If the exception is annotated with {@link ResponseStatus @ResponseStatus}, rethrow it and
 *  let the framework handle it. For example:
 *  <pre>
 *      if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null) {
 *          throw exception;
 *      }
 *  </pre>
 *  </ul>
 *
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Responsible for finding the appropriate error messages based on the given code & locale.
   */
  private final MessageSource messageSource;

  public GlobalExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Handles {@link MethodArgumentNotValidException}.
   *
   * <p><b>NOTE:</b> This exception handler is mainly used for POST method request with a JSON
   * request body.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ApiResp methodArgumentNotValidException(
      HttpServletRequest request,
      MethodArgumentNotValidException e,
      Locale locale) {
    var errorDetails = e
        .getBindingResult()
        .getFieldErrors()
        .stream()
        .map(fieldError -> toErrorDetail(fieldError, locale))
        .collect(Collectors.toList());

    log.warn(
        "method: methodArgumentNotValidException, endPoint: {} , queryString: {} , "
            + "errorDetails: {}",
        request.getRequestURI(),
        request.getQueryString(),
        errorDetails,
        e);

    return new ApiResp()
        .setCode(ARGUMENT_NOT_VALID.value())
        .setMessage(messageSource.getMessage(ARGUMENT_NOT_VALID.valueAsString(), null, locale))
        .setData(Collections.singletonMap("errorDetails", errorDetails));
  }

  /**
   * Handles {@link BindException}.
   *
   * <p><b>NOTE:</b> This exception handler is mainly used for GET method request with standard
   * query parameters.
   */
  @ExceptionHandler(BindException.class)
  public ApiResp bindException(HttpServletRequest request, BindException e, Locale locale) {
    var errorDetails = e
        .getFieldErrors()
        .stream()
        .map(fieldError -> toErrorDetail(fieldError, locale))
        .collect(Collectors.toList());

    log.warn(
        "method: bindException, endPoint: {} , queryString: {} , errorDetails: {}",
        request.getRequestURI(),
        request.getQueryString(),
        errorDetails,
        e);

    return new ApiResp()
        .setCode(ARGUMENT_NOT_VALID.value())
        .setMessage(messageSource.getMessage(ARGUMENT_NOT_VALID.valueAsString(), null, locale))
        .setData(Collections.singletonMap("errorDetails", errorDetails));
  }

  private Map<String, Object> toErrorDetail(FieldError fieldError, Locale locale) {
    var errMsg = fieldError.getDefaultMessage();
    if (errMsg.startsWith("Failed to convert property value of type")) {
      errMsg = messageSource.getMessage("alliance.invalid_data_type", null, locale);
    }
    return Map.of(
        "propertyName", fieldError.getField(),
        "message", errMsg);
  }

  /**
   * Handles {@link MissingServletRequestParameterException}.
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ApiResp missingServletRequestParameterException(
      HttpServletRequest request,
      MissingServletRequestParameterException e,
      Locale locale) {
    log.warn(
        "method: missingServletRequestParameterException, endPoint: {} , queryString: {}",
        request.getRequestURI(),
        request.getQueryString(),
        e);

    return new ApiResp()
        .setCode(MISSING_SERVLET_REQUEST_PARAMETER.value())
        .setMessage(messageSource.getMessage(
            MISSING_SERVLET_REQUEST_PARAMETER.valueAsString(),
            new Object[] {e.getParameterName()},
            locale));
  }

  /**
   * Handles {@link ConstraintViolationException}.
   *
   * <p><b>NOTE:</b> This exception handler is mainly used for POST method request with a JSON
   * request body.
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ApiResp constraintViolationException(
      HttpServletRequest request,
      ConstraintViolationException e,
      Locale locale) {
    List<Map<String, Object>> errorDetails = e.getConstraintViolations()
        .stream()
        .sorted(Comparator.comparing(this::getPropertyIndex))
        .map(v -> toErrorDetail(
            new FieldError("", v.getPropertyPath().toString(), v.getMessage()),
            locale)
        )
        .collect(Collectors.toList());

    log.warn(
        "method: constraintViolationException, endPoint: {} , queryString: {} , "
            + "errorDetails: {}",
        request.getRequestURI(),
        request.getQueryString(),
        errorDetails,
        e
    );

    return new ApiResp()
        .setCode(ARGUMENT_NOT_VALID.value())
        .setMessage(messageSource.getMessage(
            ARGUMENT_NOT_VALID.valueAsString(),
            null,
            locale
        ))
        .setData(Collections.singletonMap("errorDetails", errorDetails));
  }

  /**
   * Handles {@link HttpMessageNotReadableException}.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ApiResp httpMessageNotReadableException(
      HttpServletRequest request,
      HttpMessageNotReadableException e,
      Locale locale) {
    log.warn(
        "method: httpMessageNotReadableException, endPoint: {} , queryString: {}",
        request.getRequestURI(),
        request.getQueryString(),
        e);

    return new ApiResp()
        .setCode(HTTP_MESSAGE_NOT_READABLE.value())
        .setMessage(messageSource.getMessage(
            HTTP_MESSAGE_NOT_READABLE.valueAsString(),
            null,
            locale));
  }

  /**
   * Handles {@link MethodArgumentTypeMismatchException}.
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ApiResp methodArgumentTypeMismatchException(
      HttpServletRequest request,
      MethodArgumentTypeMismatchException e,
      Locale locale) {
    log.warn(
        "method: methodArgumentTypeMismatchException, endPoint: {} , queryString: {}",
        request.getRequestURI(),
        request.getQueryString(),
        e);

    return new ApiResp()
        .setCode(METHOD_ARGUMENT_TYPE_MISMATCH.value())
        .setMessage(
            messageSource.getMessage(
                METHOD_ARGUMENT_TYPE_MISMATCH.valueAsString(),
                new Object[] {e.getName()},
                locale));
  }

  /**
   * Handles {@link HttpMediaTypeNotSupportedException}.
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ApiResp httpMediaTypeNotSupportedException(
      HttpServletRequest request,
      HttpMediaTypeNotSupportedException e,
      Locale locale) {
    log.warn(
        "method: httpMediaTypeNotSupportedException, endPoint: {} , queryString: {}",
        request.getRequestURI(),
        request.getQueryString(),
        e);

    return new ApiResp()
        .setCode(HTTP_MEDIA_TYPE_NOT_SUPPORTED.value())
        .setMessage(
            messageSource.getMessage(
                HTTP_MEDIA_TYPE_NOT_SUPPORTED.valueAsString(),
                new Object[] {e.getContentType()},
                locale));
  }

  /**
   * Handles {@link HttpRequestMethodNotSupportedException}.
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ApiResp httpRequestMethodNotSupportedException(
      HttpServletRequest request,
      HttpRequestMethodNotSupportedException e,
      Locale locale) {
    log.warn(
        "method: httpRequestMethodNotSupportedException, endPoint: {} , queryString: {} , "
            + "supportedHttpMethods: {}",
        request.getRequestURI(),
        request.getQueryString(),
        e.getSupportedHttpMethods(),
        e);

    return new ApiResp()
        .setCode(HTTP_REQUEST_METHOD_NOT_SUPPORTED.value())
        .setMessage(
            messageSource.getMessage(
                HTTP_REQUEST_METHOD_NOT_SUPPORTED.valueAsString(),
                new Object[] {e.getMethod()},
                locale));
  }

  /**
   * Handles {@link MissingRequestHeaderException}.
   */
  @ExceptionHandler(MissingRequestHeaderException.class)
  public ApiResp missingRequestHeaderException(
      HttpServletRequest request,
      MissingRequestHeaderException e,
      Locale locale) {
    log.warn(
        "method: missingRequestHeaderException, endPoint: {} , queryString: {}",
        request.getRequestURI(),
        request.getQueryString(),
        e
    );

    return new ApiResp()
        .setCode(MISSING_REQUEST_HEADER.value())
        .setMessage(
            messageSource.getMessage(
                MISSING_REQUEST_HEADER.valueAsString(),
                new Object[] {e.getHeaderName()},
                locale));
  }

  /**
   * Handles {@link DomainException}.
   */
  @ExceptionHandler(DomainException.class)
  public ApiResp domainException(HttpServletRequest request, DomainException e, Locale locale) {
    log.warn(
        "method: domainException, endPoint: {} , queryString: {}",
        request.getRequestURI(),
        request.getQueryString(),
        e);

    String message;
    try {
      message = messageSource
          .getMessage(String.valueOf(e.getCode().value()), e.getArgs(), locale);
    } catch (NoSuchMessageException nsme) {
      log.error("Couldn't find any message for code {} under locale {}", e.getCode().value(),
          locale, nsme);
      message = "Không tìm thấy thông tin lỗi";
    }

    return new ApiResp()
        .setCode(e.getCode().value())
        .setMessage(message);
  }

  /**
   * Handles unknown errors.
   */
  @ExceptionHandler(Exception.class)
  public ApiResp exception(HttpServletRequest request, Exception e, Locale locale) {
    log.error(
        "method: exception, endPoint: {} , queryString: {}",
        request.getRequestURI(),
        request.getQueryString(),
        e);

    return new ApiResp()
        .setCode(UNKNOWN_ERROR.value())
        .setMessage(
            messageSource.getMessage(
                UNKNOWN_ERROR.valueAsString(),
                null,
                locale));
  }

  private Integer getPropertyIndex(ConstraintViolation constraintViolation) {
    for (Path.Node node : constraintViolation.getPropertyPath()) {
      if (node.getKind() == ElementKind.PROPERTY) {
        return node.getIndex();
      }
    }
    return Integer.MAX_VALUE;
  }

}
