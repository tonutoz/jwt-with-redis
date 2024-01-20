package org.example.jwt.account;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LogoutRequest {

  private String userId;

  private String refreshToken;

  @Builder
  public LogoutRequest(String userId, String refreshToken) {
    this.userId = userId;
    this.refreshToken = refreshToken;
  }

}
