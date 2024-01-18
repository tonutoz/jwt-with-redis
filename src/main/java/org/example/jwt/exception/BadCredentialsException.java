package org.example.jwt.exception;


import org.springframework.security.core.AuthenticationException;

public class BadCredentialsException extends AuthenticationException {

  private final String msg;

  public BadCredentialsException(final String msg) {
    super(msg);
    this.msg = msg;
  }

}
