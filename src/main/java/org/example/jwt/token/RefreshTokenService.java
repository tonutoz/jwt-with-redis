package org.example.jwt.token;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.jwt.exception.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
  private final RefreshTokenRepository refreshTokenRepository;

  @Transactional
  public RefreshToken findRefreshToken(final String token) {
    RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(token)
        .orElseThrow(() -> new BadCredentialsException("토큰을 찾을 수 없습니다."));
    return refreshToken;
  }


  @Transactional
  public void saveTokenInfo(String userId, String refreshToken) {
    refreshTokenRepository.save(RefreshToken.builder()
            .userId(userId)
            .refreshToken(refreshToken)
        .build());
  }

  @Transactional
  public void removeRefreshToken(String refreshToken) {
    refreshTokenRepository.findByRefreshToken(refreshToken)
        .ifPresent(refreshTokenRepository::delete);
  }

}
