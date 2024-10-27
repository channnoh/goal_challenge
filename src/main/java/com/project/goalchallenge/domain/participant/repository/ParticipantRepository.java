package com.project.goalchallenge.domain.participant.repository;

import com.project.goalchallenge.domain.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

  boolean existsByMemberIdAndChallengeId(Long memberId, Long challengeId);

  @Query("select count(*) from Participant p where p.challenge.id = :challengeId")
  Integer countByChallengeId(@Param("challengeId") Long challengeId);

  @Query("select count(*) from Participant p where p.member.id = :memberId")
  Integer countByMemberId(@Param("memberId") Long memberId);

}
