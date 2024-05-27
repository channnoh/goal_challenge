package com.project.goalchallenge.global.auth.jwt;

import com.project.goalchallenge.domain.member.type.MemberType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TokenProvider {

  private static final String KEY_ROLES = "roles";
  private final UserDetailsService userDetailsService;

  @Value("${spring.jwt.access-token-expiration-time}")
  private long ACCESS_TOKEN_EXPIRE_TIME;

  @Value("${spring.jwt.refresh-token-expiration-time}")
  private long REFRESH_TOKEN_EXPIRE_TIME;

  @Value("${spring.jwt.secret}")
  private String secretKey;
  private Key key;

  public String generateToken(String userEmail, MemberType memberType) {

    Claims claims = Jwts.claims().setSubject(userEmail);
    claims.put(KEY_ROLES, memberType);

    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(key)
        .compact();
  }


  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmail(token));

    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }


  public String getUserEmail(String token) {
    return this.parseClaims(token).getSubject();
  }


  public boolean validateToken(String token) {
    if (!StringUtils.hasText(token)) {
      return false;
    }

    Claims claims = this.parseClaims(token);
    return !claims.getExpiration().before(new Date());
  }


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

  @PostConstruct
  private void setKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

}
