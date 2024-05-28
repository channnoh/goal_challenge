package com.project.goalchallenge.domain.member.controller;

import static com.project.goalchallenge.domain.member.dto.SignUpDto.SignUpRequest;
import static com.project.goalchallenge.domain.member.dto.WithDrawDto.WithDrawRequest;

import com.project.goalchallenge.domain.member.dto.LoginDto.LoginRequest;
import com.project.goalchallenge.domain.member.dto.SignUpDto.SignUpResponse;
import com.project.goalchallenge.domain.member.dto.WithDrawDto.WithDrawResponse;
import com.project.goalchallenge.domain.member.service.MemberService;
import com.project.goalchallenge.global.auth.jwt.dto.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    SignUpResponse response = memberService.signUp(request);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
    TokenDto response = memberService.login(request);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    memberService.logout(request.getHeader(HttpHeaders.AUTHORIZATION));

    return ResponseEntity.ok().build();
  }


  @PostMapping("/withdraw")
  public ResponseEntity<?> withDraw(@RequestBody @Valid WithDrawRequest request) {
    WithDrawResponse response = memberService.withDraw(request);

    return ResponseEntity.ok(response);
  }

}
