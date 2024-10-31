package com.project.goalchallenge.domain.participant.dto;

import com.project.goalchallenge.domain.challenge.status.ChallengeStatus;
import com.project.goalchallenge.domain.participant.status.ParticipantStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

public class MyParticipantListDto {

  @Getter
  @Builder
  public static class MyParticipantListResponse {

    private String challengeName;
    private String challengePurpose;
    private ChallengeStatus challengeStatus;
    private LocalDateTime challengeStartDateTime;
    private LocalDateTime challengeEndDateTime;
    private ParticipantStatus participantStatus;
    private double challengeAchievementRate;

  }
}
