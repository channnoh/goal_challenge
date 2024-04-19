package com.project.goalchallenge.global.exception;

public class SystemException extends CustomException{

  public SystemException(ErrorCode errorCode) {
    super(errorCode);
  }
}
