package com.project.goalchallenge.domain.record.dto;

import com.project.goalchallenge.domain.record.status.RecordVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UpdateRecordDto {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateRecordRequest {

    private String textRecord;
    private RecordVisibility recordVisibility;

  }
}
