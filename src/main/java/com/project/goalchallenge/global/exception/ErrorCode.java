package com.project.goalchallenge.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // System Error
  INTERNAL_SERVER_ERROR(500, "내부 서버 오류가 발생했습니다."),
  LOCK_INTERRUPTED_ERROR(500, "Lock 인터럽트가 발생했습니다."),
  FAIL_ACQUIRE_LOCK(500, "Lock 획득에 실패했습니다."),

  // Valid Error
  BAD_REQUEST_VALID_ERROR(400, "유효성 검사에 실패했습니다."),

  // Auth Error
  ALREADY_REGISTER_USER(409, "이미 가입된 회원입니다."),
  WITHDRAWAL_USER(404, "탈퇴한 회원입니다."),
  ID_NOT_FOUND(404, "일치하는 아이디가 없습니다."),
  WRONG_PASSWORD(401, "비밀번호가 일치하지 않습니다"),

  // Challenge Error
  DUPLICATED_CHALLENGE_NAME(409, "중복된 챌린지 이름입니다."),
  CHALLENGE_NOT_FOUND(404, "존재하지 않는 챌린지입니다."),
  ALREADY_HANDLED_CHALLENGE(409, "이미 처리된 챌린지입니다."),

  // Participant Error
  CHALLENGE_NOT_RECRUITING(409, "챌린지가 모집중이 아닙니다."),
  CHALLENGE_PARTICIPANTS_FULL(409, "챌린지 참가 인원이 모두 찼습니다"),
  CHALLENGE_LIMIT_EXCEEDED(400, "신청할 수 있는 챌린지의 최대 개수를 초과했습니다."),
  CHALLENGE_ALREADY_APPLIED(409, "이미 신청된 챌린지 입니다.");

  // JwtFilterAuthenticationError
  UNAUTHORIZED(401, "인증되지 않은 사용자입니다."),

  // AccessDeniedError
  FORBIDDEN(403, "권한이 없는 사용자입니다.");

  private final int status;
  private final String message;


}
