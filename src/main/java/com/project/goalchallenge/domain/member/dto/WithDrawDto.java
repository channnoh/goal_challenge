package com.project.goalchallenge.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class WithDrawDto {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class WithDrawRequest {

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class WithDrawResponse {

    private String email;

    public static WithDrawResponse withDrawEmail(String email) {
      return WithDrawResponse.builder()
          .email(email)
          .build();
    }

  }
}
