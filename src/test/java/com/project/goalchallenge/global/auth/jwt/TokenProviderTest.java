package com.project.goalchallenge.global.auth.jwt;

import static com.project.goalchallenge.domain.member.type.MemberType.ROLE_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.goalchallenge.domain.member.entity.Member;
import com.project.goalchallenge.domain.member.repository.MemberRepository;
import com.project.goalchallenge.global.auth.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenProviderTest {

  @Autowired
  TokenProvider tokenProvider;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  CustomUserDetailsService customUserDetailsService;


  @Test
  void getToken() {

    //given
    Member member = new Member();
    member.setEmail("test@naver.com");
    member.setMemberType(ROLE_ADMIN);

    //when
    String token = tokenProvider.generateToken(member.getEmail(), member.getMemberType());

    //then
    assertThat(member.getEmail())
        .isEqualTo(tokenProvider.getUserEmail(token));
  }


}