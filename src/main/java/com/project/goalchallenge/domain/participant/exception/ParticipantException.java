package com.project.goalchallenge.domain.participant.exception;

import com.project.goalchallenge.global.exception.CustomException;
import com.project.goalchallenge.global.exception.ErrorCode;

public class ParticipantException extends CustomException {
  public ParticipantException(ErrorCode errorCode) {
    super(errorCode);
  }
}

