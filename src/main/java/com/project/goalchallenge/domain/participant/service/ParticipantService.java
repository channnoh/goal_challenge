package com.project.goalchallenge.domain.participant.service;

import static com.project.goalchallenge.domain.challenge.status.ChallengeStatus.RECRUITING;
import static com.project.goalchallenge.domain.participant.dto.ParticipantDto.fromEntity;
import static com.project.goalchallenge.global.exception.ErrorCode.CAN_NOT_CANCEL_PARTICIPANT;
import static com.project.goalchallenge.global.exception.ErrorCode.CHALLENGE_ALREADY_APPLIED;
import static com.project.goalchallenge.global.exception.ErrorCode.CHALLENGE_LIMIT_EXCEEDED;
import static com.project.goalchallenge.global.exception.ErrorCode.CHALLENGE_NOT_FOUND;
import static com.project.goalchallenge.global.exception.ErrorCode.CHALLENGE_NOT_RECRUITING;
import static com.project.goalchallenge.global.exception.ErrorCode.CHALLENGE_PARTICIPANTS_FULL;
import static com.project.goalchallenge.global.exception.ErrorCode.FAIL_ACQUIRE_LOCK;
import static com.project.goalchallenge.global.exception.ErrorCode.ID_NOT_FOUND;
import static com.project.goalchallenge.global.exception.ErrorCode.LOCK_INTERRUPTED_ERROR;
import static com.project.goalchallenge.global.exception.ErrorCode.NOT_PARTICIPANT_CHALLENGE;
import static com.project.goalchallenge.global.exception.ErrorCode.PARTICIPANT_NOT_FOUND;
import static org.springframework.data.domain.PageRequest.of;

import com.project.goalchallenge.domain.challenge.entity.Challenge;
import com.project.goalchallenge.domain.challenge.repository.ChallengeRepository;
import com.project.goalchallenge.domain.member.entity.Member;
import com.project.goalchallenge.domain.member.repository.MemberRepository;
import com.project.goalchallenge.domain.participant.dto.ChangeVisibilityDto.ChangeVisibilityRequest;
import com.project.goalchallenge.domain.participant.dto.MyParticipantListDto.MyParticipantListResponse;
import com.project.goalchallenge.domain.participant.dto.ParticipantDto;
import com.project.goalchallenge.domain.participant.entity.Participant;
import com.project.goalchallenge.domain.participant.exception.ParticipantException;
import com.project.goalchallenge.domain.participant.repository.ParticipantRepository;
import com.project.goalchallenge.global.exception.SystemException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantService {

  private final RedissonClient redissonClient;
  private final ParticipantRepository participantRepository;
  private final ChallengeRepository challengeRepository;
  private final MemberRepository memberRepository;
  private static final String CHALLENGE_LOCK_PREFIX = "challengeLock:";

  @Transactional
  public ParticipantDto applyChallenge(Long challengeId, Long userId) {
    String lockKey = CHALLENGE_LOCK_PREFIX + challengeId;
    RLock lock = redissonClient.getLock(lockKey);

    try {
      if (!lock.tryLock(5, 3, TimeUnit.SECONDS)) {
        throw new SystemException(FAIL_ACQUIRE_LOCK);
      }

      Member member = memberRepository.findById(userId)
          .orElseThrow(() -> new ParticipantException(ID_NOT_FOUND));

      Challenge challenge = challengeRepository.findById(challengeId)
          .orElseThrow(() -> new ParticipantException(CHALLENGE_NOT_FOUND));

      if (!RECRUITING.equals(challenge.getChallengeStatus())) {
        throw new ParticipantException(CHALLENGE_NOT_RECRUITING);
      }

      boolean duplicatedParticipantCheck
          = participantRepository.existsByMemberIdAndChallengeId(userId, challengeId);

      // 챌린지 중복 참여 검증
      if (duplicatedParticipantCheck) {
        throw new ParticipantException(CHALLENGE_ALREADY_APPLIED);
      }

      Integer numOfUserParticipant = participantRepository.countByMemberId(userId);

      // 유저 최대 챌린지 참여 수 검증
      if (numOfUserParticipant >= 10) {
        throw new ParticipantException(CHALLENGE_LIMIT_EXCEEDED);
      }

      Integer numOfChallengeParticipant = participantRepository.countByChallengeId(challengeId);

      // 챌린지 최대 참여자 수 검증
      if (numOfChallengeParticipant >= 10) {
        throw new ParticipantException(CHALLENGE_PARTICIPANTS_FULL);
      }

      Participant participant = new Participant();
      participant.setMember(member);
      participant.setChallenge(challenge);
      participantRepository.save(participant);

      return fromEntity(member, challenge);
    } catch (InterruptedException e) {
      throw new SystemException(LOCK_INTERRUPTED_ERROR);
    } finally {
      lock.unlock();
      log.info("unlock complete: {}", lock.getName());
    }
  }

  @Transactional
  public void changeVisibility(Long userId, Long participantId, ChangeVisibilityRequest request) {

    Participant participant = participantRepository.findById(participantId)
        .orElseThrow(() -> new ParticipantException(PARTICIPANT_NOT_FOUND));

    if (!participant.getMember().getId().equals(userId)) {
      throw new ParticipantException(NOT_PARTICIPANT_CHALLENGE);
    }

    participant.setVisibilityStatus(request.getVisibilityStatus());
  }

  @Transactional
  public void cancelChallengeParticipant(Long userId, Long participantId) {

    Participant participant = participantRepository.findById(participantId)
        .orElseThrow(() -> new ParticipantException(PARTICIPANT_NOT_FOUND));

    if (!participant.getMember().getId().equals(userId)) {
      throw new ParticipantException(NOT_PARTICIPANT_CHALLENGE);
    }

    if (!participant.getChallenge().getChallengeStatus().equals(RECRUITING)) {
      throw new ParticipantException(CAN_NOT_CANCEL_PARTICIPANT);
    }

    participantRepository.delete(participant);
  }

  public Page<MyParticipantListResponse> getMyParticipantChallengeList(Long userId, int page) {

    Pageable pageable = of(page, 10);

    Page<Participant> myParticipants
        = participantRepository.findByMemberId(pageable, userId);

    List<MyParticipantListResponse> myParticipantList =
        myParticipants.getContent().stream().map(participant -> {

          Challenge challenge = participant.getChallenge();

          return MyParticipantListResponse.builder()
              .challengeName(challenge.getChallengeName())
              .challengePurpose(challenge.getChallengePurpose())
              .challengeStatus(challenge.getChallengeStatus())
              .challengeStartDateTime(challenge.getChallengeStartDateTime())
              .challengeEndDateTime(challenge.getChallengeEndDateTime())
              .participantStatus(participant.getParticipantStatus())
              .challengeAchievementRate(participant.getChallengeAchievementRate())
              .build();

        }).toList();

    return new PageImpl<>(myParticipantList, pageable, myParticipants.getTotalElements());
  }
}
