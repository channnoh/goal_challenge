package com.project.goalchallenge.domain.challenge.repository;

import com.project.goalchallenge.domain.challenge.entity.Challenge;
import com.project.goalchallenge.domain.challenge.status.ChallengeStatus;
import com.project.goalchallenge.domain.challenge.status.RegistrationStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

  boolean existsByChallengeName(String challengeName);

  Optional<Challenge> findByChallengeName(String challengeName);

  Page<Challenge> findAllByRegistrationStatus(Pageable pageable, RegistrationStatus registrationStatus);

  List<Challenge> findByChallengeStatusAndChallengeStartDateTime(ChallengeStatus challengeStatus, LocalDateTime challengeStartDateTime);
}
