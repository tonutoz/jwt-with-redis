package org.example.jwt.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/account/")
public class UserController {

  private final UserService userService;

  @PostMapping("/sign-up")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<?> signUp(@Valid  @RequestBody final JoinRequest req) {
    log.info("signUp start");
    return ResponseEntity.ok(userService.createUser(req));
  }

  @PostMapping("/sign-in")
  public ResponseEntity<?> signIn(@Valid  @RequestBody final LoginRequest req) {
    log.info("signIn start");

    Token token = userService.doLogin(req);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Authorization", "Bearer " + token.getAccessToken());

    return new ResponseEntity<>(token, httpHeaders, HttpStatus.OK);
  }

  @PostMapping("/log-out")
  public ResponseEntity<?> logOut(@Valid  @RequestBody final LogoutRequest req) {
    log.info("log-out start");
    return ResponseEntity.ok(userService.doLogOut(req));
  }



}
