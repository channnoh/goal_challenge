package com.project.goalchallenge.domain.challenge.dto;

import com.project.goalchallenge.domain.challenge.entity.Challenge;
import com.project.goalchallenge.domain.challenge.status.ChallengeStatus;
import com.project.goalchallenge.domain.challenge.status.RegistrationStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChallengeInfoDto {

  private String challengeName;
  private String challengePurpose;
  private ChallengeStatus challengeStatus;
  private LocalDateTime suggestedDateTime;
  private RegistrationStatus registrationStatus;
  private Integer suggestedDuration;

  public static ChallengeInfoDto fromEntity(Challenge challenge) {
    return ChallengeInfoDto.builder()
        .challengeName(challenge.getChallengeName())
        .challengePurpose(challenge.getChallengePurpose())
        .challengeStatus(challenge.getChallengeStatus())
        .suggestedDateTime(challenge.getSuggestedDateTime())
        .registrationStatus(challenge.getRegistrationStatus())
        .suggestedDuration(challenge.getSuggestedDuration())
        .build();
  }
}
