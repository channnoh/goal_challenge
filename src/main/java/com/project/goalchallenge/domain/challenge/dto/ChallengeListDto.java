package com.project.goalchallenge.domain.challenge.dto;

import static com.project.goalchallenge.domain.challenge.status.ChallengeStatus.RECRUITING;

import com.project.goalchallenge.domain.challenge.entity.Challenge;
import com.project.goalchallenge.domain.challenge.status.ChallengeStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChallengeListDto {

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ChallengeListRequest {

    @Builder.Default
    private ChallengeStatus challengeStatus = RECRUITING;
  }

  @Getter
  @Builder
  public static class ChallengeListResponse {

    private String challengeName;
    private String challengePurpose;
    private LocalDateTime challengeStartDateTime;
    private LocalDateTime challengeEndDateTime;
    private ChallengeStatus challengeStatus;
    private Integer numOfParticipant;

    public static ChallengeListResponse fromEntity(Challenge challenge, int numOfParticipant) {
      return ChallengeListResponse.builder()
          .challengeName(challenge.getChallengeName())
          .challengePurpose(challenge.getChallengePurpose())
          .challengeStartDateTime(challenge.getChallengeStartDateTime())
          .challengeEndDateTime(challenge.getChallengeEndDateTime())
          .challengeStatus(challenge.getChallengeStatus())
          .numOfParticipant(numOfParticipant)
          .build();
    }
  }
}
