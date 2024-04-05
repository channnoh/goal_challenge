package com.project.goalchallenge.domain.challenge.service;

import static com.project.goalchallenge.global.exception.ErrorCode.DUPLICATED_CHALLENGE_NAME;

import com.project.goalchallenge.domain.challenge.dto.ChallengeSuggestDto;
import com.project.goalchallenge.domain.challenge.entity.Challenge;
import com.project.goalchallenge.domain.challenge.exception.ChallengeException;
import com.project.goalchallenge.domain.challenge.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeService {

  private final ChallengeRepository challengeRepository;

  // 회원이 챌린지 건의하는 메서드
  public ChallengeSuggestDto.Response suggestChallenge(ChallengeSuggestDto.Request request) {
    if (this.challengeRepository.existsByChallengeName(request.getChallengeName())) {
      throw new ChallengeException(DUPLICATED_CHALLENGE_NAME);
    }

    Challenge savedChallenge = challengeRepository.save(request.toEntity());
    return ChallengeSuggestDto.Response.fromEntity(savedChallenge);
  }

}
