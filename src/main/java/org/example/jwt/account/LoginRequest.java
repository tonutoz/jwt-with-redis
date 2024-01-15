package org.example.jwt.account;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

  @NotNull
  private String userId;

  @NotNull
  private String password;

  @Builder
  public LoginRequest(String userId, String password) {
    this.userId = userId;
    this.password = password;
  }
}
