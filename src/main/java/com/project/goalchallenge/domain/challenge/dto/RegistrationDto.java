package com.project.goalchallenge.domain.challenge.dto;

import com.project.goalchallenge.domain.challenge.status.RegistrationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDto {

  @NotBlank(message = "ChallengeName is required")
  private String challengeName;

  @NotNull(message = "RegistrationStatus is required")
  private RegistrationStatus registrationStatus;

}
