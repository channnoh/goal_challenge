package com.project.goalchallenge.domain.record.controller;

import static com.project.goalchallenge.domain.record.dto.RegisterRecordDto.RegisterRecordRequest;

import com.project.goalchallenge.domain.record.service.RecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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


}
