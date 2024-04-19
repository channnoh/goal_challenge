package com.project.goalchallenge.domain.participant.dto;

import com.project.goalchallenge.domain.challenge.entity.Challenge;
import com.project.goalchallenge.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {

  private String challengeName;
  private String userName;

  public static ParticipantDto fromEntity(Member member, Challenge challenge) {
    return ParticipantDto.builder()
        .userName(member.getUsername())
        .challengeName(challenge.getChallengeName())
        .build();
  }

}
