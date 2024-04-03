package com.project.goalchallenge.domain.member.service;

import static com.project.goalchallenge.domain.member.status.MemberStatus.DEACTIVATED;
import static com.project.goalchallenge.global.exception.ErrorCode.ALREADY_REGISTER_USER;
import static com.project.goalchallenge.global.exception.ErrorCode.ID_NOT_FOUND;
import static com.project.goalchallenge.global.exception.ErrorCode.WITHDRAWAL_USER;
import static com.project.goalchallenge.global.exception.ErrorCode.WRONG_PASSWORD;

import com.project.goalchallenge.domain.member.dto.SignInDto;
import com.project.goalchallenge.domain.member.dto.SignUpDto;
import com.project.goalchallenge.domain.member.dto.WithDrawDto;
import com.project.goalchallenge.domain.member.entity.Member;
import com.project.goalchallenge.domain.member.exception.MemberException;
import com.project.goalchallenge.domain.member.repository.MemberRepository;
import com.project.goalchallenge.global.auth.jwt.TokenProvider;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;
  private final TokenProvider tokenProvider;

  public SignUpDto.Response signUp(SignUpDto.Request request) {
    if (this.memberRepository.existsByEmail(request.getEmail())) {
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

    if (user.getMemberStatus() == DEACTIVATED) {
      throw new MemberException(WITHDRAWAL_USER);
    }

    String token = this.tokenProvider.generateToken(user.getEmail(), user.getMemberType());
    return SignInDto.Response.token(token);
  }

  @Transactional
  public WithDrawDto.Response withDraw(WithDrawDto.Request request) {

    Member user = this.memberRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new MemberException(ID_NOT_FOUND));

    if (!this.passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new MemberException(WRONG_PASSWORD);
    }

    if(user.getMemberStatus() == DEACTIVATED){
      throw new MemberException(WITHDRAWAL_USER);
    }

    user.setMemberStatus(DEACTIVATED);
    user.setWithdrawalDateTime(LocalDateTime.now());
    return WithDrawDto.Response.withDrawEmail(user.getEmail());
  }
}
