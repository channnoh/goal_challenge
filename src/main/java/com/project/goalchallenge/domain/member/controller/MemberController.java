package com.project.goalchallenge.domain.member.controller;

import com.project.goalchallenge.domain.member.dto.SignInDto;
import com.project.goalchallenge.domain.member.dto.SignUpDto;
import com.project.goalchallenge.domain.member.dto.WithDrawDto;
import com.project.goalchallenge.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/signup")
  public ResponseEntity<?> signUp(@RequestBody @Valid SignUpDto.Request request) {
    SignUpDto.Response response = this.memberService.signUp(request);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/signin")
  public ResponseEntity<?> signIn(@RequestBody @Valid SignInDto.Request request) {
    SignInDto.Response response = this.memberService.signIn(request);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/withdraw")
  public ResponseEntity<?> withDraw(@RequestBody @Valid WithDrawDto.Request request) {
    WithDrawDto.Response response = this.memberService.withDraw(request);

    return ResponseEntity.ok(response);
  }

}
