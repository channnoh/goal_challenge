package com.project.goalchallenge.domain.challenge.service;

import static com.project.goalchallenge.domain.challenge.status.ChallengeStatus.IN_PROGRESS;
import static com.project.goalchallenge.domain.challenge.status.ChallengeStatus.NOT_ENOUGH_PARTICIPANTS;
import static com.project.goalchallenge.domain.challenge.status.ChallengeStatus.RECRUITING;
import static com.project.goalchallenge.domain.challenge.status.ChallengeStatus.REGISTRATION_REJECT;
import static com.project.goalchallenge.domain.challenge.status.RegistrationStatus.APPROVE;
import static com.project.goalchallenge.domain.challenge.status.RegistrationStatus.REJECT;
import static com.project.goalchallenge.domain.challenge.status.RegistrationStatus.WAITING;
import static com.project.goalchallenge.global.exception.ErrorCode.ALREADY_HANDLED_CHALLENGE;
import static com.project.goalchallenge.global.exception.ErrorCode.CHALLENGE_NOT_FOUND;
import static com.project.goalchallenge.global.exception.ErrorCode.DUPLICATED_CHALLENGE_NAME;

import com.project.goalchallenge.domain.challenge.dto.ChallengeInfoDto;
import com.project.goalchallenge.domain.challenge.dto.ChallengeSuggestDto.ChallengeSuggestRequest;
import com.project.goalchallenge.domain.challenge.dto.ChallengeSuggestDto.ChallengeSuggestResponse;
import com.project.goalchallenge.domain.challenge.dto.RegistrationDto;
import com.project.goalchallenge.domain.challenge.entity.Challenge;
import com.project.goalchallenge.domain.challenge.exception.ChallengeException;
import com.project.goalchallenge.domain.challenge.repository.ChallengeRepository;
import com.project.goalchallenge.domain.challenge.status.RegistrationStatus;
import com.project.goalchallenge.domain.participant.entity.Participant;
import com.project.goalchallenge.domain.participant.status.ParticipantStatus;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService {

  private final ChallengeRepository challengeRepository;

  // 회원이 챌린지 건의하는 메서드
  public ChallengeSuggestResponse suggestChallenge(
      ChallengeSuggestRequest challengeSuggestRequest) {
    if (challengeRepository.existsByChallengeName(challengeSuggestRequest.getChallengeName())) {
      throw new ChallengeException(DUPLICATED_CHALLENGE_NAME);
    }

    Challenge savedChallenge = challengeRepository.save(challengeSuggestRequest.toEntity());
    return ChallengeSuggestResponse.fromEntity(savedChallenge);
  }

  // 관리자가 회원이 건의한 챌린지 목록 조회하는 메서드(등록순으로 조회)
  public Page<ChallengeInfoDto> getSuggestedChallengeList(Integer page) {

    Page<Challenge> challenges = challengeRepository.findAllByRegistrationStatus(
        PageRequest.of(page, 10, Sort.by("suggestedDateTime").descending()), WAITING);

    return challenges.map(ChallengeInfoDto::fromEntity);
  }

  // 관리자가 등록 대기 상태인 챌린지를 처리하는 메서드
  @Transactional
  public RegistrationDto registerChallenge(RegistrationDto registrationDto) {

    Challenge challenge = challengeRepository.findByChallengeName(
            registrationDto.getChallengeName())
        .orElseThrow(() -> new ChallengeException(CHALLENGE_NOT_FOUND));

    if (challenge.getRegistrationStatus() != WAITING) {
      throw new ChallengeException(ALREADY_HANDLED_CHALLENGE);
    }

    handleChallengeRequest(registrationDto.getRegistrationStatus(), challenge);

    challenge.updateChallengeRegistrationStatus(registrationDto.getRegistrationStatus());

    return registrationDto;
  }

  // 관리자가 유저 요청 챌린지 처리
  private static void handleChallengeRequest(RegistrationStatus registrationStatus,
      Challenge challenge) {
    if (registrationStatus == APPROVE) {
      challenge.updateChallengeStartAndEndDate(LocalDate.now().atStartOfDay(),
          challenge.getSuggestedDurationDay());
      challenge.updateChallengeStatus(RECRUITING);
    } else if (registrationStatus == REJECT) {
      challenge.updateChallengeStatus(REGISTRATION_REJECT);
    }
  }

  @Scheduled(cron = "0 0 0 * * *")
  public void challengeStart() {

    List<Challenge> challengeList = challengeRepository.findByChallengeStatusAndChallengeStartDateTime(
        RECRUITING,
        LocalDate.now().atStartOfDay().plusDays(7));

    if (challengeList.isEmpty()) {
      log.info("No Challenge");
    }

    for (Challenge challenge : challengeList) {
      if (!isParticipantsEnough(challenge)) {
        challenge.setChallengeStatus(NOT_ENOUGH_PARTICIPANTS);
        log.info("Challenge: {} is {}", challenge.getChallengeName(), NOT_ENOUGH_PARTICIPANTS);

        for (Participant participant : challenge.getParticipants()) {
          participant.setParticipantStatus(ParticipantStatus.FAIL);
        }
      } else {
        challenge.setChallengeStatus(IN_PROGRESS);
        log.info("Challenge: {} is {}", challenge.getChallengeName(), IN_PROGRESS);

        for (Participant participant : challenge.getParticipants()) {
          participant.setParticipantStatus(ParticipantStatus.IN_PROGRESS);
        }
      }
    }
  }

  private boolean isParticipantsEnough(Challenge challenge) {
    return challenge.getParticipants().size() >= 5;
  }
}
