package com.project.goalchallenge.domain.member.service;

import static com.project.goalchallenge.global.config.exception.ErrorCode.ALREADY_REGISTER_USER;
import static com.project.goalchallenge.global.config.exception.ErrorCode.ID_NOT_FOUND;
import static com.project.goalchallenge.global.config.exception.ErrorCode.WRONG_PASSWORD;

import com.project.goalchallenge.domain.member.dto.SignInDto;
import com.project.goalchallenge.domain.member.dto.SignUpDto;
import com.project.goalchallenge.domain.member.entity.Member;
import com.project.goalchallenge.domain.member.exception.MemberException;
import com.project.goalchallenge.domain.member.repository.MemberRepository;
import com.project.goalchallenge.global.auth.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;
  private final TokenProvider tokenProvider;

  public SignUpDto.Response signUp(SignUpDto.Request request) {
    boolean exists = this.memberRepository.existsByEmail(request.getEmail());
    if (exists) {
      throw new MemberException(ALREADY_REGISTER_USER);
    }

    request.setPassword(this.passwordEncoder.encode(request.getPassword()));
    Member savedMember = this.memberRepository.save(request.toEntity());

    return SignUpDto.Response.fromEntity(savedMember);
  }

  public SignInDto.Response signIn(SignInDto.Request request) {

    Member user = this.memberRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new MemberException(ID_NOT_FOUND));

    if (!this.passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new MemberException(WRONG_PASSWORD);
    }
    String token = this.tokenProvider.generateToken(user.getEmail(), user.getMemberType());
    return SignInDto.Response.token(token);
  }

}
