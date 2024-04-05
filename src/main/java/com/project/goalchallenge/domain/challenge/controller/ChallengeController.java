package com.project.goalchallenge.domain.challenge.controller;

import com.project.goalchallenge.domain.challenge.dto.ChallengeSuggestDto;
import com.project.goalchallenge.domain.challenge.service.ChallengeService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/challenge")
@RequiredArgsConstructor
public class ChallengeController {

  private final ChallengeService challengeService;

  @PostMapping("/suggest")
  public ResponseEntity<?> suggestChallenge(
      @RequestBody @Valid ChallengeSuggestDto.Request request) {

    ChallengeSuggestDto.Response response = this.challengeService.suggestChallenge(request);

    return ResponseEntity.ok(response);

  }
}
