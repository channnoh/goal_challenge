package com.project.goalchallenge.domain.challenge.dto;

import com.project.goalchallenge.domain.challenge.entity.Challenge;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class ChallengeSuggestDto {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @NotBlank(message = "ChallengeName is required")
    private String challengeName;

    @NotBlank(message = "ChallengePurpose is required")
    private String challengePurpose;

    @Min(value = 10, message = "챌린지의 최소 기간은 10일 입니다.")
    @Max(value = 365, message = "챌린지의 최대 기간은 1년 입니다.")
    @NotNull(message = "SuggestedDuration is required")
    private Integer suggestedDurationDay;

    public Challenge toEntity() {
      return Challenge.builder()
          .challengeName(this.challengeName)
          .challengePurpose(this.challengePurpose)
          .suggestedDurationDay(this.suggestedDurationDay)
          .build();
    }
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    private String challengeName;

    public static ChallengeSuggestDto.Response fromEntity(Challenge challenge) {
      return Response.builder()
          .challengeName(challenge.getChallengeName())
          .build();
    }

  }
}
