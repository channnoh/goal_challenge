package com.project.goalchallenge.domain.member.entity;

import static com.project.goalchallenge.domain.member.status.MemberStatus.ACTIVE;
import static com.project.goalchallenge.domain.member.status.MemberStatus.DEACTIVATED;

import com.project.goalchallenge.domain.member.status.MemberStatus;
import com.project.goalchallenge.domain.member.type.MemberType;
import com.project.goalchallenge.domain.model.BaseEntity;
import com.project.goalchallenge.domain.participant.entity.Participant;
import com.project.goalchallenge.domain.record.entity.Record;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(unique = true, nullable = false)
  private String email;

  // 유저, 관리자 권한 상태
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MemberType memberType;

  // 유저 탈퇴 여부 상태
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private MemberStatus memberStatus = ACTIVE;

  // 탈되일
  private LocalDateTime withdrawalDateTime;

  @OneToMany(mappedBy = "member")
  @Builder.Default
  private List<Participant> participants = new ArrayList<>();

  public void withDrawUpdate(LocalDateTime withdrawalDateTime) {
    this.memberStatus = DEACTIVATED;
    this.withdrawalDateTime = withdrawalDateTime;
  }

  @OneToMany(mappedBy = "member")
  private List<Record> recordList = new ArrayList<>();

}
