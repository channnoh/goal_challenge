package com.project.goalchallenge.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // System Error
  INTERNAL_SERVER_ERROR(500, "내부 서버 오류가 발생했습니다."),

  // Valid Error
  BAD_REQUEST_VALID_ERROR(400, "유효성 검사에 실패했습니다."),

  // Auth Error
  ALREADY_REGISTER_USER(409, "이미 가입된 회원입니다."),
  WITHDRAWAL_USER(404, "탈퇴한 회원입니다."),
  ID_NOT_FOUND(404, "일치하는 아이디가 없습니다."),
  WRONG_PASSWORD(401, "비밀번호가 일치하지 않습니다"),

  // Challenge Error
  DUPLICATED_CHALLENGE_NAME(409, "중복된 챌린지 이름입니다.");

  private final int status;
  private final String message;


}
