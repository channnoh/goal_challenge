package com.project.goalchallenge.global.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // System Error
  INTERNAL_SERVER_ERROR(500, "내부 서버 오류가 발생했습니다."),

  // Auth Error
  ALREADY_REGISTER_USER(409, "이미 가입된 회원입니다."),
  WITHDRAWAL_USER(404, "탈퇴한 회원입니다."),
  ID_NOT_FOUND(404, "일치하는 아이디가 없습니다."),
  WRONG_PASSWORD(401, "비밀번호가 일치하지 않습니다");

  private final int status;
  private final String message;


}
