package org.example.jwt.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.example.jwt.account.UserRole;
import org.example.jwt.exception.BadCredentialsException;
import org.example.jwt.token.RefreshToken;
import org.example.jwt.token.Token;
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

   @Value("${jwt.secret}")
   private String secret;


   @Override
   public void afterPropertiesSet() {
      //this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
      byte[] keyBytes = Decoders.BASE64.decode(secret);
      this.key = Keys.hmacShaKeyFor(keyBytes);
   }

   public Token createToken(final String userId, final UserRole role) {

      Claims claims = Jwts.claims(); // JWT payload 에 저장되는 정보
      claims.setSubject(userId);
      claims.put(AUTHORITIES_KEY, role.name());

      Date now = new Date();

      final long minute = 1000*60;

      String accessToken = createToken(claims, now, minute);

      String refreshToken =  createToken(claims, now, minute*3);

      return Token.builder()
          .userId(userId)
          .refreshToken(accessToken)
          .accessToken(refreshToken)
          .build();

   }

   public Token renewalAccessToken(final String refreshToken,final Claims claims) {
      Date now = new Date();
      String accessToken = createToken(claims, now, accessTokenInExpirationMs);

      return Token.builder()
          .userId(claims.getSubject())
          .refreshToken(accessToken)
          .accessToken(refreshToken)
          .build();
   }

   private String createToken(final Claims claims,Date nowDate, long expirationMs) {
      return Jwts.builder()
          .setClaims(claims) // 정보 저장
          .setIssuedAt(nowDate) // 토큰 발행 시간 정보
          .setExpiration(new Date(nowDate.getTime() + expirationMs))//refreshTokenInExpirationMs)) // set Expire Time
          .signWith(key,SignatureAlgorithm.HS512)  // 사용할 암호화 알고리즘과
          // signature 에 들어갈 secret값 세팅
          .compact();
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

   public boolean validateToken(final String token) {
      try {
         Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

         if(isTokenExpired(claims))
            throw new BadCredentialsException("토큰 유효 기간이 만료되었습니다.");

         return true;
      } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
         throw new BadCredentialsException("잘못된 JWT 서명입니다.");
      } catch (ExpiredJwtException e) {
         throw new BadCredentialsException("만료된 JWT 토큰입니다.");
      } catch (UnsupportedJwtException e) {
         throw new BadCredentialsException("지원되지 않는 JWT 토큰입니다.");
      } catch (IllegalArgumentException e) {
         throw new BadCredentialsException("JWT 토큰이 잘못되었습니다.");
      }
   }

   public Claims parsingToken(final String token) {
      try {
         return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
      } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
         throw new BadCredentialsException("잘못된 JWT 서명입니다.");
      } catch (ExpiredJwtException e) {
         throw new BadCredentialsException("만료된 JWT 토큰입니다.");
      } catch (UnsupportedJwtException e) {
         throw new BadCredentialsException("지원되지 않는 JWT 토큰입니다.");
      } catch (IllegalArgumentException e) {
         throw new BadCredentialsException("JWT 토큰이 잘못되었습니다.");
      }
   }


   private Boolean isTokenExpired(Claims claims) {
      final Date expiration = claims.getExpiration();
      if (expiration == null) {
         return false;
      }
      return expiration.before(new Date());
   }

}
