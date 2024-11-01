package com.project.goalchallenge.domain.participant.repository;

import com.project.goalchallenge.domain.participant.entity.Participant;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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

  @EntityGraph(attributePaths = {"challenge", "member"})
  Optional<Participant> findById(Long participantId);

  @EntityGraph(attributePaths = {"member", "challenge"})
  Optional<Participant> findByMemberIdAndChallengeId(Long userId, Long challengeId);

  Page<Participant> findByMemberId(Pageable pageable, Long memberId);
}
