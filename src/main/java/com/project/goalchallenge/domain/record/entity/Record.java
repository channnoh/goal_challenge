package com.project.goalchallenge.domain.record.entity;

import com.project.goalchallenge.domain.model.BaseEntity;
import com.project.goalchallenge.domain.participant.entity.Participant;
import com.project.goalchallenge.domain.record.status.VisibilityStatus;
import java.time.LocalDateTime;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "challenge_record")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Record extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "record_id")
  private Long id;

  @Column(nullable = false)
  private LocalDateTime recordDateTime;

  @Column(nullable = false)
  private String textRecord;

  private String imageRecordUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private VisibilityStatus visibilityStatus;

  @ManyToOne
  @JoinColumn(name = "participant_id")
  private Participant participant;
}
