package com.project.goalchallenge.domain.participant.dto;

import com.project.goalchallenge.domain.participant.status.VisibilityStatus;
import lombok.Getter;

public class ChangeVisibilityDto {

  @Getter
  public static class ChangeVisibilityRequest{

    private VisibilityStatus visibilityStatus;
  }
}
