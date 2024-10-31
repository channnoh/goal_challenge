package com.project.goalchallenge.domain.record.controller;

import static com.project.goalchallenge.domain.record.dto.RegisterRecordDto.RegisterRecordRequest;

import com.project.goalchallenge.domain.record.dto.UpdateRecordDto.UpdateRecordRequest;
import com.project.goalchallenge.domain.record.service.RecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/record")
@RequiredArgsConstructor
public class RecordController {

  private final RecordService recordService;

  @PreAuthorize("hasRole('USER')")
  @PostMapping("/{challengeId}")
  public ResponseEntity<?> registerRecord(
      @PathVariable Long challengeId, @AuthenticationPrincipal(expression = "id") Long userId,
      @Valid RegisterRecordRequest request
  ) {
    return ResponseEntity.ok(recordService.registerRecord(challengeId, userId, request));
  }

  @PreAuthorize("hasRole('USER')")
  @PatchMapping("/{recordId}")
  public ResponseEntity<?> updateRecord(
      @PathVariable Long recordId, @AuthenticationPrincipal(expression = "id") Long userId,
      @RequestBody UpdateRecordRequest request) {

    recordService.updateRecord(recordId, userId, request);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{recordId}")
  public ResponseEntity<?> deleteRecord(
      @PathVariable Long recordId, @AuthenticationPrincipal(expression = "id") Long userId) {

    recordService.deleteRecord(recordId, userId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/list/{participantId}")
  public ResponseEntity<?> getMyRecord(
      @PathVariable Long participantId, @AuthenticationPrincipal(expression = "id") Long userId,
      @RequestParam(defaultValue = "0") Integer page) {

    return ResponseEntity.ok(recordService.getMyRecordList(participantId, userId, page));
  }
}
