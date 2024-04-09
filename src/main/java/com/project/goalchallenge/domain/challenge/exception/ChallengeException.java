package com.project.goalchallenge.domain.challenge.exception;

import com.project.goalchallenge.global.exception.CustomException;
import com.project.goalchallenge.global.exception.ErrorCode;

public class ChallengeException extends CustomException {
  public ChallengeException(ErrorCode errorCode) {
    super(errorCode);
  }
}
