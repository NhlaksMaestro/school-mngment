package com.exp.test.exception;

/** Not Found exception. */
public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
    super(message);
  }
}
