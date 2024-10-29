package com.project.goalchallenge.domain.participant.entity;

import static com.project.goalchallenge.domain.participant.status.ParticipantStatus.WAITING;

import com.project.goalchallenge.domain.challenge.entity.Challenge;
import com.project.goalchallenge.domain.member.entity.Member;
import com.project.goalchallenge.domain.participant.status.ParticipantStatus;
import com.project.goalchallenge.domain.record.entity.Record;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "participant_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private ParticipantStatus participantStatus = WAITING;

  @Builder.Default
  private double ChallengeAchievementRate = 0.0;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "challenge_id")
  private Challenge challenge;

  @OneToMany(mappedBy = "participant")
  private List<Record> records = new ArrayList<>();
}
