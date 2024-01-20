package org.example.jwt.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jwt.account.JoinRequest;
import org.example.jwt.account.LogoutRequest;
import org.example.jwt.account.LoginRequest;
import org.example.jwt.account.UserService;
import org.example.jwt.token.RefreshToken;
import org.example.jwt.token.Token;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth/")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/reissue")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<?> signUp(@Valid  @RequestBody final AuthRequest req) {
    log.info("reissue start");

    Token token = authService.reIssueToken(req.getRefreshToken());

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Authorization", "Bearer " + token.getAccessToken());

    return new ResponseEntity<>(token, httpHeaders, HttpStatus.OK);

  }

}
