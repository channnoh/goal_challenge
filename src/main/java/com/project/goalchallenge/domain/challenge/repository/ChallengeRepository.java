package com.project.goalchallenge.domain.challenge.repository;

import com.project.goalchallenge.domain.challenge.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

  boolean existsByChallengeName(String challengeName);
}
