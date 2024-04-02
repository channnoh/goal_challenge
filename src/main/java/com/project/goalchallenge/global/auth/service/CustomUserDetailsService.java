package com.project.goalchallenge.global.auth.service;

import com.project.goalchallenge.domain.member.entity.Member;
import com.project.goalchallenge.domain.member.repository.MemberRepository;
import com.project.goalchallenge.global.auth.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Couldn't find user"));
    return new CustomUserDetails(member.getMemberType(), member.getPassword(), member.getEmail());

  }
}
