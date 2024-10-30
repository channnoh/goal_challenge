package com.project.goalchallenge.domain.record.entity;

import static com.project.goalchallenge.domain.record.status.RecordVisibility.PRIVATE;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.project.goalchallenge.domain.member.entity.Member;
import com.project.goalchallenge.domain.model.BaseEntity;
import com.project.goalchallenge.domain.participant.entity.Participant;
import com.project.goalchallenge.domain.record.status.RecordVisibility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "record")
public class Record extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "record_id")
  private Long id;

  @Column(nullable = false)
  private String textRecord;

  private String imageRecord;

  @Default
  @Enumerated(EnumType.STRING)
  private RecordVisibility recordVisibility = PRIVATE;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "participant_id")
  private Participant participant;
}
