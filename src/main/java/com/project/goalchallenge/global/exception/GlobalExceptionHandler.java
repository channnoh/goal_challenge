package com.project.goalchallenge.global.exception;

import static com.project.goalchallenge.global.exception.ErrorCode.BAD_REQUEST_VALID_ERROR;
import static com.project.goalchallenge.global.exception.ErrorCode.FORBIDDEN;
import static com.project.goalchallenge.global.exception.ErrorCode.INTERNAL_SERVER_ERROR;

import com.project.goalchallenge.domain.challenge.exception.ChallengeException;
import com.project.goalchallenge.domain.member.exception.MemberException;
import com.project.goalchallenge.domain.participant.exception.ParticipantException;
import com.project.goalchallenge.domain.record.exception.RecordException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MemberException.class)
  public ErrorResponse handleMemberException(MemberException e) {
    log.error("Exception \"{}({})\" is occurred.", e.getErrorCode(), e.getErrorCode().getMessage());

    return new ErrorResponse(e.getErrorCode(), e.getErrorCode().getStatus(),
        e.getErrorCode().getMessage());
  }

  @ExceptionHandler(ChallengeException.class)
  public ErrorResponse handleChallengeException(ChallengeException e) {
    log.error("Exception \"{}({})\" is occurred.", e.getErrorCode(), e.getErrorCode().getMessage());

    return new ErrorResponse(e.getErrorCode(), e.getErrorCode().getStatus(),
        e.getErrorCode().getMessage());
  }

  @ExceptionHandler(ParticipantException.class)
  public ErrorResponse handleParticipantException(ParticipantException e) {
    log.error("Exception \"{}({})\" is occurred.", e.getErrorCode(), e.getErrorCode().getMessage());

    return new ErrorResponse(e.getErrorCode(), e.getErrorCode().getStatus(),
        e.getErrorCode().getMessage());
  }

  @ExceptionHandler(RecordException.class)
  public ErrorResponse handleRecordException(RecordException e) {
    log.error("Exception \"{}({})\" is occurred.", e.getErrorCode(), e.getErrorCode().getMessage());

    return new ErrorResponse(e.getErrorCode(), e.getErrorCode().getStatus(),
        e.getErrorCode().getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    List<String> errors = e.getBindingResult().getAllErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .toList();
    log.error("Exception \"{}({})\" is occurred.", BAD_REQUEST_VALID_ERROR, errors.get(0));
    return new ErrorResponse(
        BAD_REQUEST_VALID_ERROR, 400, errors.get(0));

  }

  @ExceptionHandler(AccessDeniedException.class)
  public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {

    log.error("Exception \"{}({})\" is occurred.", FORBIDDEN, e.getMessage());
    return new ErrorResponse(
        FORBIDDEN, FORBIDDEN.getStatus(), FORBIDDEN.getMessage());
  }

  @ExceptionHandler(SystemException.class)
  public ErrorResponse handleSystemException(SystemException e) {
    log.error("Exception \"{}({})\" is occurred.", e.getErrorCode(), e.getErrorCode().getMessage());

    return new ErrorResponse(e.getErrorCode(), e.getErrorCode().getStatus(),
        e.getErrorCode().getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ErrorResponse handleException(Exception e) {
    log.error("Exception {} is occurred.", e.getMessage());

    return new ErrorResponse(
        INTERNAL_SERVER_ERROR, 500, INTERNAL_SERVER_ERROR.getMessage());
  }
}
