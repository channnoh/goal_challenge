package com.project.goalchallenge.global.config.exception;

import static com.project.goalchallenge.global.config.exception.ErrorCode.INTERNAL_SERVER_ERROR;

import com.project.goalchallenge.domain.member.exception.MemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MemberException.class)
  public ErrorResponse handleMemberException(MemberException e) {
    log.error("Exception {} is occurred.", e.getMessage());

    return new ErrorResponse(e.getErrorCode(), e.getStatus(), e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ErrorResponse handleException(Exception e) {
    log.error("Exception {} is occurred.", e.getMessage());

    return new ErrorResponse(
        INTERNAL_SERVER_ERROR, 500, INTERNAL_SERVER_ERROR.getMessage());
  }
}
