package org.example.jwt.config;

import lombok.RequiredArgsConstructor;

import org.example.jwt.security.JwtAccessDeniedHandler;
import org.example.jwt.security.JwtAuthenticationEntryPoint;

import org.example.jwt.security.JwtAuthenticationFilter;
import org.example.jwt.security.JwtSecurityConfig;
import org.example.jwt.security.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@EnableMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true
)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

  private final CorsFilter corsFilter;

  private final TokenProvider tokenProvider;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf(AbstractHttpConfigurer::disable);
    httpSecurity.cors(Customizer.withDefaults());
    //HTTP 기본 인증 비활성화
    httpSecurity.formLogin(AbstractHttpConfigurer::disable);
    httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
    httpSecurity.headers(
        headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
    httpSecurity.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
        SessionCreationPolicy.STATELESS));

    httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    httpSecurity.exceptionHandling((exceptionHandling) -> exceptionHandling
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .accessDeniedHandler(jwtAccessDeniedHandler)
    );

    //권한 규칙 구성 시작
    httpSecurity.authorizeHttpRequests(
        authorize -> authorize
            .requestMatchers("/account/sign-up").permitAll()
            .requestMatchers("/account/sign-in").permitAll()
            .requestMatchers("/api/redis/").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/v3/api-docs/**").permitAll()
            .requestMatchers("/swagger-resources/**").permitAll()
            .requestMatchers("/webjars/**").permitAll()
            .anyRequest().authenticated()
    );

    return httpSecurity.build();
  }
}