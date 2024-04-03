package com.project.goalchallenge.domain.member.exception;

import com.project.goalchallenge.global.exception.CustomException;
import com.project.goalchallenge.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class MemberException extends CustomException {

  public MemberException(ErrorCode errorCode) {
    super(errorCode);
  }
}
