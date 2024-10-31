package com.project.goalchallenge.domain.record.repository;

import com.project.goalchallenge.domain.record.entity.Record;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

  @Query("select case when count(*) > 0 then true else false end "
      + "from Record r "
      + "where r.participant.id = :participantId "
      + "and year (r.createdDateTime) = year (:recordDate)"
      + "and month (r.createdDateTime) = month (:recordDate)"
      + "and day (r.createdDateTime) = day (:recordDate)")
  boolean existsRecordByParticipantIdAndRecordDate(Long participantId, LocalDate recordDate);

  @EntityGraph(attributePaths = {"participant"})
  Page<Record> findAllByParticipantIdAndMemberId(Pageable pageable, Long participantId, Long memberId);
}
