package com.project.goalchallenge.domain.challenge.dto;

import com.project.goalchallenge.domain.challenge.status.RegistrationStatus;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
