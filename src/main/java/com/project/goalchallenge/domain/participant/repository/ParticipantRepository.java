package com.project.goalchallenge.domain.participant.repository;

import com.project.goalchallenge.domain.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

  boolean existsByMemberIdAndChallengeId(Long memberId, Long challengeId);
}
