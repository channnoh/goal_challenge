package com.project.goalchallenge.domain.participant.controller;

import com.project.goalchallenge.domain.participant.dto.ChangeVisibilityDto.ChangeVisibilityRequest;
import com.project.goalchallenge.domain.participant.service.ParticipantService;
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
@RequestMapping("/participant")
@RequiredArgsConstructor
public class ParticipantController {

  private final ParticipantService participantService;

  @PreAuthorize("hasRole('USER')")
  @PostMapping("/{challengeId}")
  public ResponseEntity<?> applyChallenge(
      @PathVariable Long challengeId, @AuthenticationPrincipal(expression = "id") Long userId) {
    return ResponseEntity.ok(participantService.applyChallenge(challengeId, userId));
  }

  @PreAuthorize("hasRole('USER')")
  @DeleteMapping("/{participantId}")
  public ResponseEntity<?> cancelParticipant(
      @PathVariable Long participantId, @AuthenticationPrincipal(expression = "id") Long userId) {
    participantService.cancelChallengeParticipant(userId, participantId);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{participantId}")
  public ResponseEntity<?> changeParticipantVisibility(
      @PathVariable Long participantId, @AuthenticationPrincipal(expression = "id") Long userUd,
      @RequestBody ChangeVisibilityRequest request
  ) {
    participantService.changeVisibility(userUd, participantId, request);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/list")
  public ResponseEntity<?> getMyParticipantList(
      @AuthenticationPrincipal(expression = "id") Long userId,
      @RequestParam(defaultValue = "0") Integer page) {
    return ResponseEntity.ok(participantService.getMyParticipantChallengeList(userId, page));
  }

}
