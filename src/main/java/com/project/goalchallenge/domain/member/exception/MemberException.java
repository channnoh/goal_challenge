package com.project.goalchallenge.domain.member.exception;

import com.project.goalchallenge.global.config.exception.CustomException;
import com.project.goalchallenge.global.config.exception.ErrorCode;
import lombok.Getter;

@Getter
public class MemberException extends CustomException {

  public MemberException(ErrorCode errorCode) {
    super(errorCode);
  }
}
