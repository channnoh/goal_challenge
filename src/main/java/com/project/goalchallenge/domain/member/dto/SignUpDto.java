package com.project.goalchallenge.domain.member.dto;

import com.project.goalchallenge.domain.member.entity.Member;
import com.project.goalchallenge.domain.member.type.MemberType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class SignUpDto {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SignUpRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @Email(message = "유효하지 않은 이메일 형식입니다.",
        regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String email;

    @NotNull(message = "MemberType is required")
    private MemberType memberType;


    public Member toEntity() {
      return Member.builder()
          .username(this.username)
          .password(this.password)
          .email(this.email)
          .memberType(this.memberType)
          .build();
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SignUpResponse {

    private String email;

    public static SignUpResponse fromEntity(Member member) {
      return SignUpResponse.builder()
          .email(member.getEmail())
          .build();
    }
  }

}
