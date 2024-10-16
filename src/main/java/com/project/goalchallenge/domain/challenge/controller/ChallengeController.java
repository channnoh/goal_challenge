package com.project.goalchallenge.domain.challenge.controller;

import com.project.goalchallenge.domain.challenge.dto.ChallengeSuggestDto.ChallengeSuggestRequest;
import com.project.goalchallenge.domain.challenge.dto.ChallengeSuggestDto.ChallengeSuggestResponse;
import com.project.goalchallenge.domain.challenge.dto.RegistrationDto;
import com.project.goalchallenge.domain.challenge.service.ChallengeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/challenge")
@RequiredArgsConstructor
public class ChallengeController {

  private final ChallengeService challengeService;

  @PostMapping("/suggest")
  public ResponseEntity<?> suggestChallenge(
      @RequestBody @Valid ChallengeSuggestRequest request) {

    ChallengeSuggestResponse response = challengeService.suggestChallenge(request);

    return ResponseEntity.ok(response);

  }

  @GetMapping("/suggested/list")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> suggestedChallengeList(
      @RequestParam(defaultValue = "0") Integer page) {

    return ResponseEntity.ok(challengeService.getSuggestedChallengeList(page));
  }

  @PatchMapping("/suggested")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> registerChallenge(@RequestBody @Valid RegistrationDto registrationDto) {
    return ResponseEntity.ok(challengeService.registerChallenge(registrationDto));
  }


}
