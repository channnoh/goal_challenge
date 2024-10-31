package com.project.goalchallenge.domain.record.dto;

import com.project.goalchallenge.domain.record.status.RecordVisibility;
import lombok.Builder;
import lombok.Getter;

public class RecordListDto {

  @Getter
  @Builder
  public static class RecordListResponse {

    private String challengeName;
    private String textRecord;
    private String imageRecord;
    private RecordVisibility recordVisibility;
    private double challengeAchievementRate;
  }
}
