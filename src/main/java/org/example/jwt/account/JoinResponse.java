package org.example.jwt.account;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinResponse {

  private String userId;

  private String code;

  private String msg;

  @Builder
  public JoinResponse(String userId, String code, String msg) {
    this.userId = userId;
    this.code = code;
    this.msg = msg;
  }
}
