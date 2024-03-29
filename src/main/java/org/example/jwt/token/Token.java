package org.example.jwt.token;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Token {

  private String userId;

  private String refreshToken;

  private String accessToken;

  @Builder
  public Token(String userId, String refreshToken, String accessToken) {
    this.userId = userId;
    this.refreshToken = refreshToken;
    this.accessToken = accessToken;
  }
}
