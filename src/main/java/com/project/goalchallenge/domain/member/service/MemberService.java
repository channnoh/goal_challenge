package com.project.goalchallenge.domain.member.service;

import static com.project.goalchallenge.domain.member.dto.SignUpDto.SignUpRequest;
import static com.project.goalchallenge.domain.member.dto.SignUpDto.SignUpResponse;
import static com.project.goalchallenge.domain.member.status.MemberStatus.DEACTIVATED;
import static com.project.goalchallenge.global.exception.ErrorCode.ALREADY_REGISTER_USER;
import static com.project.goalchallenge.global.exception.ErrorCode.ID_NOT_FOUND;
import static com.project.goalchallenge.global.exception.ErrorCode.WITHDRAWAL_USER;
import static com.project.goalchallenge.global.exception.ErrorCode.WRONG_PASSWORD;

import com.project.goalchallenge.domain.member.dto.LoginDto.LoginRequest;
import com.project.goalchallenge.domain.member.dto.WithDrawDto.WithDrawRequest;
import com.project.goalchallenge.domain.member.dto.WithDrawDto.WithDrawResponse;
import com.project.goalchallenge.domain.member.entity.Member;
import com.project.goalchallenge.domain.member.exception.MemberException;
import com.project.goalchallenge.domain.member.repository.MemberRepository;
import com.project.goalchallenge.domain.member.type.MemberType;
import com.project.goalchallenge.global.auth.jwt.TokenProvider;
import com.project.goalchallenge.global.auth.jwt.dto.TokenDto;
import com.project.goalchallenge.global.config.redis.RedisService;
import java.time.LocalDateTime;
import java.util.Date;
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
  private final RedisService redisService;

  // 회원가입
  public SignUpResponse signUp(SignUpRequest signUpRequest) {
    if (memberRepository.existsByEmail(signUpRequest.getEmail())) {
      throw new MemberException(ALREADY_REGISTER_USER);
    }

    signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    Member savedMember = memberRepository.save(signUpRequest.toEntity());

    return SignUpResponse.fromEntity(savedMember);
  }

  // 로그인
  public TokenDto login(LoginRequest loginRequest) {

    Member member = memberRepository.findByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new MemberException(ID_NOT_FOUND));

    if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
      throw new MemberException(WRONG_PASSWORD);
    }

    if (member.getMemberStatus() == DEACTIVATED) {
      throw new MemberException(WITHDRAWAL_USER);
    }

    return generateToken(member.getEmail(), member.getMemberType());
  }

  // 토큰 발급
  public TokenDto generateToken(String email, MemberType memberType) {
    // Redis 에 RT가 존재할 경우 -> 삭제
    if (redisService.existData("RT:" + email)) {
      redisService.deleteData("RT:" + email);
    }

    // AT, RT 생성 및 Redis 에 RT 저장
    TokenDto tokenDto = tokenProvider.generateToken(email, memberType);
    long expiration =
        tokenProvider.getTokenExpirationTime(tokenDto.getRefreshToken()) - new Date().getTime();

    redisService.setDataExpire("RT:" + email, tokenDto.getRefreshToken(), expiration);
    return tokenDto;
  }

  // 회원 탈퇴
  @Transactional
  public WithDrawResponse withDraw(WithDrawRequest withDrawRequest) {

    Member user = this.memberRepository.findByEmail(withDrawRequest.getEmail())
        .orElseThrow(() -> new MemberException(ID_NOT_FOUND));

    if (!this.passwordEncoder.matches(withDrawRequest.getPassword(), user.getPassword())) {
      throw new MemberException(WRONG_PASSWORD);
    }

    if (user.getMemberStatus() == DEACTIVATED) {
      throw new MemberException(WITHDRAWAL_USER);
    }

    user.setMemberStatus(DEACTIVATED);
    user.setWithdrawalDateTime(LocalDateTime.now());
    return WithDrawResponse.withDrawEmail(user.getEmail());
  }
}
