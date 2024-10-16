package com.project.goalchallenge.global.config.security;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.project.goalchallenge.global.auth.jwt.JwtAuthenticationFilter;
import com.project.goalchallenge.global.auth.jwt.TokenProvider;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final TokenProvider tokenProvider;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors
            .configurationSource(request -> {
              CorsConfiguration config = new CorsConfiguration();

              config.setAllowedOrigins(List.of());
              config.setAllowedMethods(Collections.singletonList("*"));
              config.setAllowCredentials(true);
              config.setAllowedHeaders(Collections.singletonList("*"));
              config.setMaxAge(3600L);

              config.setExposedHeaders(Arrays.asList("Authorization", "Set_Cookie"));
              return config;
            }))

        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .sessionManagement(configurer -> configurer
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .requestMatchers(anyRequest()).permitAll()
            .requestMatchers(requestAuthenticated()).authenticated()
        )
        // JWT Filter
        .addFilterBefore(new JwtAuthenticationFilter(tokenProvider),
            UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  // 모든 사용자 접근 가능 경로
  private RequestMatcher[] anyRequest() {
    List<RequestMatcher> requestMatchers = List.of(
        antMatcher(POST, "/member/signup"),
        antMatcher(POST, "/member/login")
    );
    return requestMatchers.toArray(RequestMatcher[]::new);
  }

  // 유저 접근 가능
  private RequestMatcher[] requestAuthenticated() {
    List<RequestMatcher> requestMatchers = List.of(
        antMatcher(POST, "/member/withdraw"),
        antMatcher(POST, "/member/logout"),
        antMatcher(POST, "/challenge/**")
    );
    return requestMatchers.toArray(RequestMatcher[]::new);
  }
}