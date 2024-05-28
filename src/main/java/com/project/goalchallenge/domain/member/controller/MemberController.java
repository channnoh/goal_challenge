package com.project.goalchallenge.domain.member.controller;

import static com.project.goalchallenge.domain.member.dto.SignUpDto.SignUpRequest;

import com.project.goalchallenge.domain.member.dto.LoginDto.LoginRequest;
import com.project.goalchallenge.domain.member.dto.SignUpDto.SignUpResponse;
import com.project.goalchallenge.domain.member.dto.WithDrawDto;
import com.project.goalchallenge.domain.member.service.MemberService;
import com.project.goalchallenge.global.auth.jwt.dto.TokenDto;
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
  public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest request) {
    SignUpResponse response = this.memberService.signUp(request);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
    TokenDto response = this.memberService.login(request);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/withdraw")
  public ResponseEntity<?> withDraw(@RequestBody @Valid WithDrawDto.Request request) {
    WithDrawDto.Response response = this.memberService.withDraw(request);

    return ResponseEntity.ok(response);
  }

}
