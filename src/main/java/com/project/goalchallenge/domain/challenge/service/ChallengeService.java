package com.project.goalchallenge.domain.challenge.service;

import static com.project.goalchallenge.domain.challenge.status.RegistrationStatus.WAITING;
import static com.project.goalchallenge.global.exception.ErrorCode.DUPLICATED_CHALLENGE_NAME;

import com.project.goalchallenge.domain.challenge.dto.ChallengeInfoDto;
import com.project.goalchallenge.domain.challenge.dto.ChallengeSuggestDto;
import com.project.goalchallenge.domain.challenge.entity.Challenge;
import com.project.goalchallenge.domain.challenge.exception.ChallengeException;
import com.project.goalchallenge.domain.challenge.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
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

  // 관리자가 회원이 건의한 챌린지 목록 조회하는 메서드(등록순으로 조회)
  public Page<ChallengeInfoDto> getSuggestedChallengeList(Integer page) {

    Page<Challenge> challenges = challengeRepository.findAllByRegistrationStatus(
        PageRequest.of(page, 10, Sort.by("suggestedDateTime").descending()), WAITING);

    if (page >= challenges.getTotalPages()) {
      challenges = challengeRepository.findAllByRegistrationStatus(
          PageRequest.of(0, 10, Sort.by("suggestedDateTime").descending()), WAITING);
    }

    return challenges.map(ChallengeInfoDto::fromEntity);
  }
}
