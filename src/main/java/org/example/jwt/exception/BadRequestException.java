package org.example.jwt.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{

  private final String errCode;
  private final String msg;

  public BadRequestException(String errCode,String msg) {
    super(msg);
    this.errCode = errCode;
    this.msg = msg;
  }

}
