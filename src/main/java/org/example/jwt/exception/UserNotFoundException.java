package org.example.jwt.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException{

  private final String errCode;
  private final String msg;

  public UserNotFoundException(String errCode,String msg) {
    super(msg);
    this.errCode = errCode;
    this.msg = msg;
  }

}
