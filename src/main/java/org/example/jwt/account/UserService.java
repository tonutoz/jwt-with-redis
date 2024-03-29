package org.example.jwt.account;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jwt.exception.AlreadyExistsUserException;
import org.example.jwt.exception.BadRequestException;
import org.example.jwt.exception.UserNotFoundException;
import org.example.jwt.security.TokenProvider;
import org.example.jwt.token.RefreshToken;
import org.example.jwt.token.RefreshTokenRepository;
import org.example.jwt.token.Token;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder encoder;

  private final TokenProvider tokenProvider;

  private final RefreshTokenRepository tokenRepository;

  @Transactional
  public JoinResponse createUser(final JoinRequest req) {

    if(userRepository.findByUserId(req.getUserId()).isPresent())
      throw new AlreadyExistsUserException("ERR-001","유저가 존재합니다 유저아이디="+req.getUserId());

    log.debug("zzzzzzzzzzzzz1");
    User user = User.builder()
        .userId(req.getUserId())
        .password(encoder.encode(req.getPassword()))
        .email(req.getEMail())
        .nickname(req.getNickname())
        .userRole(UserRole.USER)
        .build();;

    userRepository.saveAndFlush(user);

    return JoinResponse.builder()
        .userId(user.getUserId())
        .code("SUCCESS")
        .msg("성공적으로 만들었습니다")
        .build();

  }

  @org.springframework.transaction.annotation.Transactional
  public Token doLogin(final LoginRequest req) {

    User user= userRepository.findByUserId(req.getUserId()).orElseThrow(()->new UserNotFoundException("ERR-002" ,"존재 하지않는 아이디 입니다."));

    if(!encoder.matches(req.getPassword(), user.getPassword()))
      throw new BadRequestException("ERR-003","비밀번호가 틀립니다");

    Token token =  tokenProvider.createToken(req.getUserId(), user.getUserRole());
    log.info("token {}" , token);
    // 로그인 시 토큰 정보를 redis에서 delete함

    RefreshToken refreshToken= RefreshToken.builder()
        .userId(token.getUserId())
        .refreshToken(token.getRefreshToken())
        .build();

    tokenRepository.delete(refreshToken);
    tokenRepository.save(refreshToken);

    return token;
  }

  public LogoutResponse doLogOut(final LogoutRequest req) {

    RefreshToken refreshToken = tokenRepository.findByRefreshToken(req.getRefreshToken()).orElseThrow(()->new UserNotFoundException("ERR-003" ,"토큰 정보가 존재하지않습니다."));

    tokenRepository.delete(refreshToken);

    return LogoutResponse.builder()
        .userId(req.getUserId())
        .code("SUCCESS")
        .msg("성공적으로 로그아웃하였습니다.")
        .build();
  }

}
