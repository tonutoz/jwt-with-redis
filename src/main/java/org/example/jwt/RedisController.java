package org.example.jwt;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jwt.token.RefreshToken;
import org.example.jwt.token.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/redis/")
@Slf4j
public class RedisController {

  private static final String KEY = "transactions";

  private final StringRedisTemplate template;

  private final RefreshTokenService service;

  @PostMapping()
  public void addToken(@RequestBody RefreshToken token) {
    log.info("{}",token);
    service.saveTokenInfo(token.getUserId(),token.getRefreshToken(),token.getAccessToken());

  }

  @DeleteMapping("{accessToken}")
  public void getKeyValues(@PathVariable final String accessToken) {
    service.removeRefreshToken(accessToken);
  }

}
