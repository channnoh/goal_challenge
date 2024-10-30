package com.project.goalchallenge.domain.record.service;

import static com.project.goalchallenge.domain.participant.status.ParticipantStatus.IN_PROGRESS;
import static com.project.goalchallenge.domain.record.dto.RegisterRecordDto.RegisterRecordResponse.fromEntity;
import static com.project.goalchallenge.global.exception.ErrorCode.ALREADY_REGISTER_CHALLENGE_RECORD;
import static com.project.goalchallenge.global.exception.ErrorCode.NOT_CORRECT_CHALLENGE_RECORD_DATE;
import static com.project.goalchallenge.global.exception.ErrorCode.NOT_CORRECT_PARTICIPANT_STATUS;
import static com.project.goalchallenge.global.exception.ErrorCode.NOT_PARTICIPANT_CHALLENGE;

import com.project.goalchallenge.domain.participant.entity.Participant;
import com.project.goalchallenge.domain.participant.repository.ParticipantRepository;
import com.project.goalchallenge.domain.record.dto.RegisterRecordDto.RegisterRecordRequest;
import com.project.goalchallenge.domain.record.dto.RegisterRecordDto.RegisterRecordResponse;
import com.project.goalchallenge.domain.record.entity.Record;
import com.project.goalchallenge.domain.record.exception.RecordException;
import com.project.goalchallenge.domain.record.repository.RecordRepository;
import com.project.goalchallenge.global.config.s3.S3Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {

  private final S3Service s3Service;
  private final RecordRepository recordRepository;
  private final ParticipantRepository participantRepository;

  @Transactional
  public RegisterRecordResponse registerRecord(Long challengeId, Long userId,
      RegisterRecordRequest request) {

    LocalDate today = LocalDate.now();

    Participant participant
        = participantRepository.findByMemberIdAndChallengeId(userId, challengeId)
        .orElseThrow(() -> new RecordException(NOT_PARTICIPANT_CHALLENGE));

    if (participant.getParticipantStatus() != IN_PROGRESS) {
      throw new RecordException(NOT_CORRECT_PARTICIPANT_STATUS);
    }

    if (!request.getRecordDate().equals(today)) {
      throw new RecordException(NOT_CORRECT_CHALLENGE_RECORD_DATE);
    }

    if (recordRepository.existsRecordByParticipantIdAndRecordDate(participant.getId(),
        request.getRecordDate())) {
      throw new RecordException(ALREADY_REGISTER_CHALLENGE_RECORD);
    }

    Record record = Record.builder()
        .textRecord(request.getTextRecord())
        .recordVisibility(request.getRecordVisibility())
        .build();

    if (request.getImageRecord() != null) {
      String recordImageUrl = s3Service.uploadFile
          (request.getImageRecord(),
              participant.getMember().getEmail() + "/" + participant.getChallenge()
                  .getChallengeName());

      record.setImageRecord(recordImageUrl);
    }

    record.registerRecord(participant.getMember(), participant);
    recordRepository.save(record);

    return fromEntity(
        participant.getChallenge().getChallengeName(), LocalDateTime.now());
  }


}
