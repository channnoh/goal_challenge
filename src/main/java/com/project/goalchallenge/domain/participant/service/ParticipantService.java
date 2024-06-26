package com.project.goalchallenge.domain.participant.service;

import static com.project.goalchallenge.domain.challenge.status.ChallengeStatus.RECRUITING;
import static com.project.goalchallenge.global.exception.ErrorCode.CHALLENGE_ALREADY_APPLIED;
import static com.project.goalchallenge.global.exception.ErrorCode.CHALLENGE_LIMIT_EXCEEDED;
import static com.project.goalchallenge.global.exception.ErrorCode.CHALLENGE_NOT_FOUND;
import static com.project.goalchallenge.global.exception.ErrorCode.CHALLENGE_NOT_RECRUITING;
import static com.project.goalchallenge.global.exception.ErrorCode.CHALLENGE_PARTICIPANTS_FULL;
import static com.project.goalchallenge.global.exception.ErrorCode.FAIL_ACQUIRE_LOCK;
import static com.project.goalchallenge.global.exception.ErrorCode.ID_NOT_FOUND;
import static com.project.goalchallenge.global.exception.ErrorCode.LOCK_INTERRUPTED_ERROR;

import com.project.goalchallenge.domain.challenge.entity.Challenge;
import com.project.goalchallenge.domain.challenge.repository.ChallengeRepository;
import com.project.goalchallenge.domain.member.entity.Member;
import com.project.goalchallenge.domain.member.repository.MemberRepository;
import com.project.goalchallenge.domain.participant.dto.ParticipantDto;
import com.project.goalchallenge.domain.participant.entity.Participant;
import com.project.goalchallenge.domain.participant.exception.ParticipantException;
import com.project.goalchallenge.domain.participant.repository.ParticipantRepository;
import com.project.goalchallenge.global.exception.SystemException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantService {

  private final RedissonClient redissonClient;
  private final ParticipantRepository participantRepository;
  private final ChallengeRepository challengeRepository;
  private final MemberRepository memberRepository;
  private static final Integer MAX_PARTICIPANTS = 10;
  private static final String CHALLENGE_LOCK_PREFIX = "challengeLock:";
  private static final String PARTICIPANT_KEY_PREFIX = "participant:";
  private static final String PARTICIPANTS_COUNT_KEY_PREFIX = "participantsCount:";

  public ParticipantDto applyChallenge(Long challengeId, Long userId) {
    String lockKey = CHALLENGE_LOCK_PREFIX + challengeId;
    RLock lock = redissonClient.getLock(lockKey);
    String isParticipateKey = PARTICIPANT_KEY_PREFIX + userId + ":" + challengeId;
    String participantsCountKey = PARTICIPANTS_COUNT_KEY_PREFIX + challengeId;

    Member member = memberRepository.findById(userId)
        .orElseThrow(() -> new ParticipantException(ID_NOT_FOUND));

    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ParticipantException(CHALLENGE_NOT_FOUND));

    if (!RECRUITING.equals(challenge.getChallengeStatus())) {
      throw new ParticipantException(CHALLENGE_NOT_RECRUITING);
    }

    try {
      if (!lock.tryLock(5, TimeUnit.SECONDS)) {
        throw new SystemException(FAIL_ACQUIRE_LOCK);
      }

      // 챌린지 중복 참여 검증 & 저장
      RMap<String, Boolean> participantsDuplicateCache = duplicateCheck(
          challengeId, userId, isParticipateKey);

      if (member.getParticipants().size() >= 5) {
        participantsDuplicateCache.put(isParticipateKey, false);
        throw new ParticipantException(CHALLENGE_LIMIT_EXCEEDED);
      }

      // 챌린지 참여자 수 검증
      participantsCount(challengeId, participantsCountKey);

      Participant participant = new Participant();
      participant.setMember(member);
      participant.setChallenge(challenge);
      participantRepository.save(participant);

      return ParticipantDto.fromEntity(member, challenge);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new SystemException(LOCK_INTERRUPTED_ERROR);
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
  }

  private void participantsCount(Long challengeId, String participantsCountKey) {
    RAtomicLong participantsCount = redissonClient.getAtomicLong(participantsCountKey);

    // 캐시 또는 DB에서 참가자 수를 초기화
    if (!participantsCount.isExists()) {
      participantsCount.set(challengeRepository.findById(challengeId)
          .orElseThrow(() -> new ParticipantException(CHALLENGE_NOT_FOUND))
          .getParticipants().size());
    }

    if (participantsCount.incrementAndGet() > MAX_PARTICIPANTS) {
      participantsCount.decrementAndGet(); // 참가자 수를 다시 감소시켜 롤백
      throw new ParticipantException(CHALLENGE_PARTICIPANTS_FULL);
    }
  }

  private RMap<String, Boolean> duplicateCheck(Long challengeId, Long userId,
      String isParticipateKey) {
    RMap<String, Boolean> participantsDuplicateCache = redissonClient.getMap(
        "challengeParticipantsDuplicateCache");

    // 캐시에서 키 조회
    Boolean isAlreadyParticipant = participantsDuplicateCache.getOrDefault(
        isParticipateKey, false);

    // 캐시 미스: DB에서 확인
    checkCacheMiss(challengeId, userId, isParticipateKey, isAlreadyParticipant,
        participantsDuplicateCache);

    // 챌린지 중복 참여 검증 & 저장
    checkParticipantDuplicateAndStore(participantsDuplicateCache, isParticipateKey);
    return participantsDuplicateCache;
  }

  private void checkCacheMiss(Long challengeId, Long userId, String isParticipateKey,
      Boolean isAlreadyParticipant, RMap<String, Boolean> participantsDuplicateCache) {
    if (!isAlreadyParticipant) {
      if (participantRepository.existsByMemberIdAndChallengeId(userId,
          challengeId)) {
        log.info("db에서 예외발생");
        throw new ParticipantException(CHALLENGE_ALREADY_APPLIED);
      } else {
        participantsDuplicateCache.put(isParticipateKey, false);
      }
    }
  }

  private static void checkParticipantDuplicateAndStore(
      RMap<String, Boolean> participantsDuplicateCache, String participantDuplicateKey) {
    if (participantsDuplicateCache.get(participantDuplicateKey)) {
      throw new ParticipantException(CHALLENGE_ALREADY_APPLIED);
    } else {
      participantsDuplicateCache.put(participantDuplicateKey, true);
    }
  }


}
