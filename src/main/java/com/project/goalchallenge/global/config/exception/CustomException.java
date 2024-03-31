package com.project.goalchallenge.global.config.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  private final ErrorCode errorCode;
  private final int status;
  private final String message;

  public CustomException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.status = errorCode.getStatus();
    this.message = errorCode.getMessage();
  }
}
