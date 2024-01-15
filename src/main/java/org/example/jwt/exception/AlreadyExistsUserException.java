package org.example.jwt.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class AlreadyExistsUserException extends RuntimeException {

  private final String errCode;
  private final String msg;

  public AlreadyExistsUserException(String errCode, String msg) {
    super(msg);
    this.errCode = errCode;
    this.msg = msg;
  }

}
