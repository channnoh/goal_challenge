package com.project.goalchallenge.global.auth.jwt;

import com.project.goalchallenge.domain.member.type.MemberType;
import com.project.goalchallenge.global.auth.jwt.dto.TokenDto;
import com.project.goalchallenge.global.auth.model.CustomUserDetails;
import com.project.goalchallenge.global.auth.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

  private static final String KEY_ROLES = "role";
  private final CustomUserDetailsService userDetailsService;

  @Value("${spring.jwt.access-token-expiration-time}")
  private long ACCESS_TOKEN_EXPIRE_TIME;

  @Value("${spring.jwt.refresh-token-expiration-time}")
  private long REFRESH_TOKEN_EXPIRE_TIME;

  @Value("${spring.jwt.secret}")
  private String secretKey;

  private static final String TOKEN_PREFIX = "Bearer ";

  private Key key;

  // Access token, Refresh token 생성
  public TokenDto generateToken(String email, MemberType memberType) {

    Date now = new Date();

    Date accessTokenExpiration = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
    String accessToken = Jwts.builder()
        .setExpiration(accessTokenExpiration)
        .setSubject("access-token")
        .claim("email", email)
        .claim(KEY_ROLES, memberType)
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();

    Date refreshTokenExpiration = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);
    String refreshToken = Jwts.builder()
        .setExpiration(refreshTokenExpiration)
        .setSubject("refresh-token")
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();

    log.info("[TokenProvider] : accessToken, refreshToken 생성 완료");

    return TokenDto.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
  public Authentication getAuthentication(String token) {
    CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
        this.getUserEmail(token));

    log.info("[JwtTokenProvider] 토큰 인증 정보 조회 완료, userName : {}", userDetails.getUsername());
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }


  // Token Claims 에서 email 꺼내오기
  public String getUserEmail(String token) {
    return this.parseClaims(token).get("email").toString();
  }


  // Token 유효성 검증
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.error("[TokenProvider] Invalid JWT Token");
    } catch (ExpiredJwtException e) {
      log.error("[TokenProvider] Expired JWT Token");
    } catch (UnsupportedJwtException e) {
      log.error("[TokenProvider] Unsupported JWT Token");
    } catch (IllegalArgumentException e) {
      log.error("[TokenProvider] JWT claims string is empty");
    }
    return false;
  }


  // Token 으로 부터 정보 추출
  private Claims parseClaims(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  // 토큰 만료 시간 확인
  public long getTokenExpirationTime(String token) {
    if (token.startsWith(TOKEN_PREFIX)) {
      token = token.substring(TOKEN_PREFIX.length());
    }
    return parseClaims(token).getExpiration().getTime();
  }

  @PostConstruct
  private void setKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

}
