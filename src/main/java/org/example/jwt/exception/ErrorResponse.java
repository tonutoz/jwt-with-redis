package org.example.jwt.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

  private String code;

  private String message;

  @Builder
  public ErrorResponse(String code, String message) {
    this.code = code;
    this.message = message;
  }
}
