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

  public ParticipantDto applyChallenge(Long memberId, Long challengeId) {
    String lockKey = CHALLENGE_LOCK_PREFIX + challengeId;
    RLock lock = redissonClient.getLock(lockKey);
    String participantDuplicateKey = PARTICIPANT_KEY_PREFIX + memberId + ":" + challengeId;
    String participantsCountKey = PARTICIPANTS_COUNT_KEY_PREFIX + challengeId;

    try {
      if (!lock.tryLock(5, TimeUnit.SECONDS)) {
        throw new SystemException(FAIL_ACQUIRE_LOCK);
      }

      RMap<String, Boolean> participantsDuplicateCache = redissonClient.getMap(
          "challengeParticipantsDuplicateCache");

      // 캐시에서 키 조회
      Boolean isAlreadyParticipant = participantsDuplicateCache.getOrDefault(
          participantDuplicateKey, false);

      // 캐시 미스: DB에서 확인
      checkParticipantDuplicateCacheMiss(memberId, challengeId, isAlreadyParticipant,
          participantsDuplicateCache, participantDuplicateKey);

      // 챌린지 중복 참여 검증 & 저장
      checkParticipantDuplicateAndStore(participantsDuplicateCache, participantDuplicateKey);

      Member member = memberRepository.findById(memberId)
          .orElseThrow(() -> new ParticipantException(ID_NOT_FOUND));

      if (member.getParticipants().size() >= 5) {
        participantsDuplicateCache.put(participantDuplicateKey, false);
        throw new ParticipantException(CHALLENGE_LIMIT_EXCEEDED);
      }

      Challenge challenge = challengeRepository.findById(challengeId)
          .orElseThrow(() -> new ParticipantException(CHALLENGE_NOT_FOUND));

      if (!RECRUITING.equals(challenge.getChallengeStatus())) {
        throw new ParticipantException(CHALLENGE_NOT_RECRUITING);
      }

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


  private void checkParticipantDuplicateCacheMiss(Long memberId, Long challengeId,
      Boolean isAlreadyParticipant,
      RMap<String, Boolean> participantsDuplicateCache, String participantDuplicateKey) {
    if (!isAlreadyParticipant) {
      boolean existsInDb = participantRepository.existsByMemberIdAndChallengeId(memberId,
          challengeId);
      if (existsInDb) {
        throw new ParticipantException(CHALLENGE_ALREADY_APPLIED);
      } else {
        participantsDuplicateCache.put(participantDuplicateKey, false);
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
