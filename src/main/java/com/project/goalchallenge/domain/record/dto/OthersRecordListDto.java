package com.project.goalchallenge.domain.record.dto;

import com.project.goalchallenge.domain.challenge.status.ChallengeStatus;
import com.project.goalchallenge.domain.participant.status.ParticipantStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

public class OthersRecordListDto {

  @Getter
  @Builder
  public static class OthersRecordListResponse {

    private String username;
    private String challengeName;
    private String challengePurpose;
    private ChallengeStatus challengeStatus;
    private double challengeAchievementRate;
    private String textRecord;
    private String imageRecord;
    private LocalDateTime recordDateTime;
    private ParticipantStatus participantStatus;

  }
}
