package com.project.goalchallenge.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class WithDrawDto {

  @Getter
  public static class WithDrawRequest {

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class WithDrawResponse {

    private String email;

    public static WithDrawResponse withDrawResponse(String email) {
      return WithDrawResponse.builder()
          .email(email)
          .build();
    }

  }
}
