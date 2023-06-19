package com.exp.test.exception;

import lombok.Getter;
import lombok.Setter;

/** Error message. */
@Setter
@Getter
public class ErrorMessage {
  private int code;
  private String message;
}
