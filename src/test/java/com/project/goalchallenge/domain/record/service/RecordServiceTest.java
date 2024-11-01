package com.project.goalchallenge.domain.record.service;

import static com.project.goalchallenge.domain.challenge.status.ChallengeStatus.IN_PROGRESS;
import static com.project.goalchallenge.domain.challenge.status.RegistrationStatus.APPROVE;
import static com.project.goalchallenge.global.exception.ErrorCode.ALREADY_REGISTER_CHALLENGE_RECORD;
import static com.project.goalchallenge.global.exception.ErrorCode.NOT_CORRECT_CHALLENGE_RECORD_DATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.project.goalchallenge.domain.challenge.entity.Challenge;
import com.project.goalchallenge.domain.challenge.repository.ChallengeRepository;
import com.project.goalchallenge.domain.member.entity.Member;
import com.project.goalchallenge.domain.member.repository.MemberRepository;
import com.project.goalchallenge.domain.member.type.MemberType;
import com.project.goalchallenge.domain.participant.entity.Participant;
import com.project.goalchallenge.domain.participant.repository.ParticipantRepository;
import com.project.goalchallenge.domain.participant.status.ParticipantStatus;
import com.project.goalchallenge.domain.record.dto.RegisterRecordDto.RegisterRecordRequest;
import com.project.goalchallenge.domain.record.dto.RegisterRecordDto.RegisterRecordResponse;
import com.project.goalchallenge.domain.record.exception.RecordException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RecordServiceTest {

  @Autowired
  private RecordService recordService;

  @Autowired
  private ParticipantRepository participantRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private ChallengeRepository challengeRepository;

  @Test
  void registerRecord_O() {
    //given
    Member member = Member.builder()
        .username("test")
        .password("test123!")
        .email("test@naver.com")
        .memberType(MemberType.ROLE_USER)
        .build();

    Challenge challenge = Challenge.builder()
        .challengeName("challenge")
        .challengePurpose("test")
        .registrationStatus(APPROVE)
        .challengeStatus(IN_PROGRESS)
        .suggestedDurationDay(10)
        .build();

    Member savedMember = memberRepository.save(member);
    Challenge savedChallenge = challengeRepository.save(challenge);

    Participant participant = Participant.builder()
        .participantStatus(ParticipantStatus.IN_PROGRESS)
        .member(savedMember)
        .challenge(savedChallenge)
        .build();

    participantRepository.save(participant);

    RegisterRecordRequest recordRequest = RegisterRecordRequest.builder()
        .textRecord("test 용 기록")
        .recordDate(LocalDate.now())
        .build();

    //when
    RegisterRecordResponse result = recordService.registerRecord(
        savedChallenge.getId(), savedMember.getId(), recordRequest);

    //then
    assertThat(result.getChallengeName()).isEqualTo("challenge");
  }

  @Test
  void registerRecord_date_exception() {
    //given
    Member member = Member.builder()
        .username("test")
        .password("test123!")
        .email("test@naver.com")
        .memberType(MemberType.ROLE_USER)
        .build();

    Challenge challenge = Challenge.builder()
        .challengeName("challenge")
        .challengePurpose("test")
        .registrationStatus(APPROVE)
        .challengeStatus(IN_PROGRESS)
        .suggestedDurationDay(10)
        .build();

    Member savedMember = memberRepository.save(member);
    Challenge savedChallenge = challengeRepository.save(challenge);

    Participant participant = Participant.builder()
        .participantStatus(ParticipantStatus.IN_PROGRESS)
        .member(savedMember)
        .challenge(savedChallenge)
        .build();

    participantRepository.save(participant);

    RegisterRecordRequest recordRequest = RegisterRecordRequest.builder()
        .textRecord("test 용 기록")
        .recordDate(LocalDate.parse("2024-05-30"))
        .build();

    //when
    RecordException recordException = assertThrows(RecordException.class,
        () -> recordService.registerRecord(
            savedChallenge.getId(), savedMember.getId(), recordRequest));

    //then
    assertThat(recordException.getErrorCode()).isEqualTo(NOT_CORRECT_CHALLENGE_RECORD_DATE);
  }

  @Test
  void registerRecord_duplicated_exception() {
    //given
    Member member = Member.builder()
        .username("test")
        .password("test123!")
        .email("test@naver.com")
        .memberType(MemberType.ROLE_USER)
        .build();

    Challenge challenge = Challenge.builder()
        .challengeName("challenge")
        .challengePurpose("test")
        .registrationStatus(APPROVE)
        .challengeStatus(IN_PROGRESS)
        .suggestedDurationDay(10)
        .build();

    Member savedMember = memberRepository.save(member);
    Challenge savedChallenge = challengeRepository.save(challenge);

    Participant participant = Participant.builder()
        .participantStatus(ParticipantStatus.IN_PROGRESS)
        .member(savedMember)
        .challenge(savedChallenge)
        .build();

    participantRepository.save(participant);

    RegisterRecordRequest recordRequest = RegisterRecordRequest.builder()
        .textRecord("test 용 기록")
        .recordDate(LocalDate.now())
        .build();

    recordService.registerRecord(
        savedChallenge.getId(), savedMember.getId(), recordRequest);

    RegisterRecordRequest duplicatedRecordRequest = RegisterRecordRequest.builder()
        .textRecord("test 용 기록2")
        .recordDate(LocalDate.now())
        .build();

    //when
    RecordException recordException = assertThrows(RecordException.class,
        () -> recordService.registerRecord(
            savedChallenge.getId(), savedMember.getId(), duplicatedRecordRequest));

    //then
    assertThat(recordException.getErrorCode()).isEqualTo(ALREADY_REGISTER_CHALLENGE_RECORD);
  }

}