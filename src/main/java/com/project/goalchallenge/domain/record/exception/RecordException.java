package com.project.goalchallenge.domain.record.exception;

import com.project.goalchallenge.global.exception.CustomException;
import com.project.goalchallenge.global.exception.ErrorCode;

public class RecordException extends CustomException {

  public RecordException(ErrorCode errorCode) {
    super(errorCode);
  }
}
