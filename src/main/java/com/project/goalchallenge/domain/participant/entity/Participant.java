package com.project.goalchallenge.domain.participant.entity;

import static com.project.goalchallenge.domain.participant.status.ParticipantStatus.WAITING;

import com.project.goalchallenge.domain.challenge.entity.Challenge;
import com.project.goalchallenge.domain.member.entity.Member;
import com.project.goalchallenge.domain.participant.status.ParticipantStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "challenge_participant")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "challenge_participant_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private ParticipantStatus participantStatus = WAITING;

  @Builder.Default
  private Double ChallengeAchievementRate = 0.0;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "challenge_id")
  private Challenge challenge;
}
