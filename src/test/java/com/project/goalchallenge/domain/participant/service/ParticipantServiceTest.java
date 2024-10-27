package com.project.goalchallenge.domain.participant.service;

import static com.project.goalchallenge.domain.challenge.status.ChallengeStatus.RECRUITING;
import static com.project.goalchallenge.domain.member.type.MemberType.ROLE_USER;
import static com.project.goalchallenge.global.exception.ErrorCode.CHALLENGE_NOT_FOUND;
import static com.project.goalchallenge.global.exception.ErrorCode.CHALLENGE_PARTICIPANTS_FULL;
import static com.project.goalchallenge.global.exception.ErrorCode.ID_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.project.goalchallenge.domain.challenge.entity.Challenge;
import com.project.goalchallenge.domain.challenge.exception.ChallengeException;
import com.project.goalchallenge.domain.challenge.repository.ChallengeRepository;
import com.project.goalchallenge.domain.member.entity.Member;
import com.project.goalchallenge.domain.member.exception.MemberException;
import com.project.goalchallenge.domain.member.repository.MemberRepository;
import com.project.goalchallenge.domain.participant.entity.Participant;
import com.project.goalchallenge.domain.participant.exception.ParticipantException;
import com.project.goalchallenge.domain.participant.repository.ParticipantRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ParticipantServiceTest {

  @Autowired
  private ChallengeRepository challengeRepository;

  @Autowired
  private ParticipantRepository participantRepository;

  @Autowired
  private ParticipantService participantService;

  @Autowired
  private MemberRepository memberRepository;

  @Test
  void 챌린지_락적용x() throws ExecutionException, InterruptedException {

    int numberOfThreads = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    CountDownLatch latch = new CountDownLatch(numberOfThreads);

    Future<Long> createMember
        = executorService.submit(() -> {
      Member member = Member.builder()
          .username("user")
          .password("test123!")
          .email("test@naver.com")
          .memberType(ROLE_USER)
          .build();

      return memberRepository.save(member).getId();
    });

    Future<Long> createChallenge
        = executorService.submit(() -> {

      Challenge challenge = new Challenge();
      challenge.setChallengeName("test");
      challenge.setChallengePurpose("test");
      challenge.setSuggestedDurationDay(10);

      return challengeRepository.save(challenge).getId();
    });

    final Long memberId = createMember.get();
    final Long challengeId = createChallenge.get();

    for (int i = 0; i < numberOfThreads; i++) {
      executorService.submit(() -> {
        try {
          Member member = memberRepository.findById(memberId)
              .orElseThrow(() -> new MemberException(ID_NOT_FOUND));

          Challenge challenge = challengeRepository.findById(challengeId)
              .orElseThrow(() -> new ParticipantException(CHALLENGE_NOT_FOUND));

          Integer numOfChallengeParticipant = participantRepository.countByChallengeId(challengeId);

          if (numOfChallengeParticipant >= 10) {
            throw new ParticipantException(CHALLENGE_PARTICIPANTS_FULL);
          }

          Participant participant = new Participant();
          participant.setMember(member);
          participant.setChallenge(challenge);
          participantRepository.save(participant);
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();

    Challenge result = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeException(CHALLENGE_NOT_FOUND));

    Integer num = participantRepository.countByChallengeId(result.getId());

    assertNotEquals(10, num);
  }

  @Test
  void 챌린지_락적용o() throws ExecutionException, InterruptedException {

    int numberOfThreads = 50;
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    CountDownLatch latch = new CountDownLatch(numberOfThreads);

    Future<Long> createChallenge
        = executorService.submit(() -> {

      Challenge challenge = new Challenge();
      challenge.setChallengeName("test");
      challenge.setChallengePurpose("test");
      challenge.setSuggestedDurationDay(10);
      challenge.setChallengeStatus(RECRUITING);

      return challengeRepository.save(challenge).getId();
    });

    final Long challengeId = createChallenge.get();

    for (int i = 0; i < numberOfThreads; i++) {
      executorService.submit(() -> {

        Long memberId = saveMember(Thread.currentThread().getId());
        try {
          participantService.applyChallenge(challengeId, memberId);
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();

    Challenge result = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new ChallengeException(CHALLENGE_NOT_FOUND));

    int size = result.getParticipants().size();

    assertEquals(10, size);
  }

  private Long saveMember(long i) {
    Member member = Member.builder()
        .username("user" + i)
        .password("test" + i + "123!")
        .email("test" + i + "@naver.com")
        .memberType(ROLE_USER)
        .build();

    return memberRepository.save(member).getId();
  }
}