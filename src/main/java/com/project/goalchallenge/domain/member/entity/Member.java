package com.project.goalchallenge.domain.member.entity;

import static com.project.goalchallenge.domain.member.status.MemberStatus.ACTIVE;
import static com.project.goalchallenge.domain.member.type.MemberType.ROLE_USER;

import com.project.goalchallenge.domain.member.status.MemberStatus;
import com.project.goalchallenge.domain.member.type.MemberType;
import com.project.goalchallenge.domain.model.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(unique = true, nullable = false)
  private String email;

  // 유저, 관리자 권한 상태
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private MemberType memberType = ROLE_USER;

  // 유저 탈퇴 여부 상태
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private MemberStatus memberStatus = ACTIVE;

  // 탈되일
  private LocalDateTime withdrawalDatetime;
}
