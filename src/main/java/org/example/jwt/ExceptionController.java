package org.example.jwt;

import org.example.jwt.exception.AlreadyExistsUserException;
import org.example.jwt.exception.BadRequestException;
import org.example.jwt.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionController  extends ResponseEntityExceptionHandler {

  @ExceptionHandler(AlreadyExistsUserException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> handleValidateException(AlreadyExistsUserException e) {
    return handleExceptionInternal(e.getErrCode(), e.getMsg());
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> handleValidateException(BadRequestException e) {
    return handleExceptionInternal(e.getErrCode(), e.getMsg());
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> handleValidateException(UserNotFoundException e) {
    return handleExceptionInternal(e.getErrCode(), e.getMsg());
  }


  private ResponseEntity<?> handleExceptionInternal(final String errCode,final String errMsg) {
    return ResponseEntity.ok(ErrorResponse.builder().code(errCode).message(errMsg).build());
  }

}
