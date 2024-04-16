package com.project.goalchallenge.domain.participant.controller;

import com.project.goalchallenge.domain.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/participant")
@RequiredArgsConstructor
public class ParticipantController {

  private final ParticipantService participantService;

  @PostMapping("/{userId}/{challengeId}")
  public ResponseEntity<?> applyChallenge(@PathVariable Long userId, @PathVariable Long challengeId) {
    return ResponseEntity.ok(participantService.applyChallenge(userId, challengeId));
  }

}
