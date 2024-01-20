package org.example.jwt.auth;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jwt.exception.BadCredentialsException;
import org.example.jwt.security.TokenProvider;
import org.example.jwt.token.RefreshToken;
import org.example.jwt.token.RefreshTokenRepository;
import org.example.jwt.token.Token;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final TokenProvider tokenProvider;

  private final RefreshTokenRepository tokenRepository;

  public Token reIssueToken(final String refreshToken) {
    log.info("refreshToken {}", refreshToken);
    //TODO ... 어차피 redis TTL이면 날라갈텐디?
    RefreshToken redisToken = tokenRepository.findByRefreshToken(refreshToken).orElseThrow(()-> new BadCredentialsException("토큰 유효 기간이 만료되었습니다."));

    final Claims tokenBody = tokenProvider.parsingToken(redisToken.getRefreshToken());
    log.info("tokenBody {}", tokenBody.getSubject());
    return tokenProvider.renewalAccessToken(redisToken.getRefreshToken(),tokenBody);
  }


}
