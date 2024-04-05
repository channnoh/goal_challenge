package com.project.goalchallenge.domain.challenge.entity;

import com.project.goalchallenge.domain.challenge.status.ChallengeStatus;
import com.project.goalchallenge.domain.challenge.status.RegistrationStatus;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Challenge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String challengeName;

  @Column(nullable = false)
  private String challengePurpose;

  /*
     챌린지 등록될 때 건의된 챌린지 기간을 바탕으로 필드값 설정
   */
  private LocalDateTime challengeStartDateTime;
  private LocalDateTime challengeEndDateTime;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private ChallengeStatus challengeStatus = ChallengeStatus.REGISTRATION_WAITING;

  @CreatedDate
  private LocalDateTime suggestedDateTime; // 회원이 챌린지 건의한 날짜

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private RegistrationStatus registrationStatus = RegistrationStatus.WAITING; // 챌린지 등록상태

  @Column(nullable = false)
  private Integer suggestedDuration; // 회원이 건의한 챌린지 기간


}
