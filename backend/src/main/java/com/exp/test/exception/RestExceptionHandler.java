package com.exp.test.exception;

import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/** Exception handler. */
@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

  /**
   * Handle constraint violation exception.
   *
   * @param exception ConstraintViolationException
   * @return response of error message
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorMessage> handleConstraintViolationException(
      ConstraintViolationException exception) {
    ErrorMessage errorMessage = new ErrorMessage();
    errorMessage.setCode(HttpStatus.BAD_REQUEST.value());
    errorMessage.setMessage(exception.getMessage());
    return ResponseEntity.badRequest().body(errorMessage);
  }

  /**
   * Handle method argument notValid exception.
   *
   * @param exception MethodArgumentNotValidException
   * @return response of error message
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessage> handleInValidException(
      MethodArgumentNotValidException exception) {
    ErrorMessage errorMessage = new ErrorMessage();
    errorMessage.setCode(HttpStatus.BAD_REQUEST.value());
    errorMessage.setMessage(
        exception.getFieldErrors().stream()
            .map(e -> String.join(": ", e.getField(), e.getDefaultMessage()))
            .collect(Collectors.joining(", ")));
    return ResponseEntity.badRequest().body(errorMessage);
  }

  /**
   * Handle parameters binding notValid exception.
   *
   * @param exception BindException
   * @return response of error message
   */
  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorMessage> handleInValidBindException(BindException exception) {
    ErrorMessage errorMessage = new ErrorMessage();
    errorMessage.setCode(HttpStatus.BAD_REQUEST.value());
    errorMessage.setMessage(
        exception.getFieldErrors().stream()
            .map(e -> String.join(": ", e.getField(), e.getDefaultMessage()))
            .collect(Collectors.joining(", ")));
    return ResponseEntity.badRequest().body(errorMessage);
  }

  /**
   * Handle NotFound exception.
   *
   * @param exception NotFoundException
   * @return response of error message
   */
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorMessage> handleNotFoundException(NotFoundException exception) {
    ErrorMessage errorMessage = new ErrorMessage();
    errorMessage.setCode(HttpStatus.NOT_FOUND.value());
    errorMessage.setMessage(exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
  }

  /**
   * Handle BadRequest exception.
   *
   * @param exception BadRequestException
   * @return response of error message
   */
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorMessage> handleBadRequestException(BadRequestException exception) {
    ErrorMessage errorMessage = new ErrorMessage();
    errorMessage.setCode(HttpStatus.BAD_REQUEST.value());
    errorMessage.setMessage(exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
  }

  /**
   * Handle unknown exception.
   *
   * @param exception Exception
   * @return response of error message
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorMessage> handleException(Exception exception) {
    log.error("unknown exception: ", exception);
    ErrorMessage errorMessage = new ErrorMessage();
    errorMessage.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    errorMessage.setMessage(
        "Unexpected Error occurred, please retry later or contact administrator");
    return ResponseEntity.internalServerError().body(errorMessage);
  }
}
