package org.example.jwt.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.example.jwt.token.RefreshToken;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider implements InitializingBean {

   private static final String AUTHORITIES_KEY = "auth";

   private Key key;

   @Value("${jwt.token.access.expiration}")
   private long accessTokenInExpirationMs;

   @Value("${jwt.token.refresh.expiration}")
   private long refreshTokenInExpirationMs;

   @Override
   public void afterPropertiesSet() {
      this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
   }

   public RefreshToken createToken(final String userId) {

      Claims claims = Jwts.claims(); // JWT payload 에 저장되는 정보
      claims.put("userId", userId);

      Date now = new Date();

      String accessToken = Jwts.builder()
          .setClaims(claims) // 정보 저장
          .setIssuedAt(now) // 토큰 발행 시간 정보
          .setExpiration(new Date(now.getTime() + accessTokenInExpirationMs)) // set Expire Time
          .signWith(key)  // 사용할 암호화 알고리즘과
          // signature 에 들어갈 secret값 세팅
          .compact();

      String refreshToken =  Jwts.builder()
          .setClaims(claims) // 정보 저장
          .setIssuedAt(now) // 토큰 발행 시간 정보
          .setExpiration(new Date(now.getTime() + refreshTokenInExpirationMs)) // set Expire Time
          .signWith(key)  // 사용할 암호화 알고리즘과
          // signature 에 들어갈 secret값 세팅
          .compact();

      return RefreshToken.builder()
          .userId(userId)
          .refreshToken(accessToken)
          .accessToken(refreshToken)
          .build();

   }


   public Authentication getAuthentication(String token) {
      Claims claims = Jwts
              .parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token)
              .getBody();

      Collection<? extends GrantedAuthority> authorities =
         Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

      User principal = new User(claims.getSubject(), "", authorities);

      return new UsernamePasswordAuthenticationToken(principal, token, authorities);
   }

   public boolean validateToken(String token) {
      try {
         Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
         return true;
      } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
         log.info("잘못된 JWT 서명입니다.");
      } catch (ExpiredJwtException e) {
         log.info("만료된 JWT 토큰입니다.");
      } catch (UnsupportedJwtException e) {
         log.info("지원되지 않는 JWT 토큰입니다.");
      } catch (IllegalArgumentException e) {
         log.info("JWT 토큰이 잘못되었습니다.");
      }
      return false;
   }
}
