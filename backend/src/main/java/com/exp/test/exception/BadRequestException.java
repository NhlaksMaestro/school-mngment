package com.exp.test.exception;

/** Bad Request exception. */
public class BadRequestException extends RuntimeException {
  public BadRequestException(String message) {
    super(message);
  }
}
