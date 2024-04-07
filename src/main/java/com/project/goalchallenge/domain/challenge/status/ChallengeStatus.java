package com.project.goalchallenge.domain.challenge.status;

public enum ChallengeStatus {
  REGISTRATION_WAITING, // 등록 대기
  REGISTRATION_REJECT, // 등록 거절
  RECRUITING, // 모집중
  IN_PROGRESS, // 진행중
  FAILED, // 실패
  COMPLETED, // 완료
  NOT_ENOUGH_PARTICIPANTS // 인원 미달
}
