package org.example.jwt.token;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.jwt.exception.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
  private final RefreshTokenRepository refreshTokenRepository;

  @Transactional
  public RefreshToken findTokeninfo(final String accessToken) {
    RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken)
        .orElseThrow(() -> new BadCredentialsException("토큰을 찾을 수 없습니다."));
    return refreshToken;
  }


  @Transactional
  public void saveTokenInfo(String userId, String refreshToken, String accessToken) {
    refreshTokenRepository.save(RefreshToken.builder()
            .userId(userId)
            .refreshToken(refreshToken)
            .accessToken(accessToken)
        .build());
  }

  @Transactional
  public void removeRefreshToken(String accessToken) {
    refreshTokenRepository.findByAccessToken(accessToken)
        .ifPresent(refreshTokenRepository::delete);
  }
}
