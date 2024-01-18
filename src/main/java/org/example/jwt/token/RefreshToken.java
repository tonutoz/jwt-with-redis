package org.example.jwt.token;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@ToString
@Getter
@RedisHash(value = "jwtToken", timeToLive = 60*60*24*3)
public class RefreshToken {

  @Id
  private String userId;

  private final String refreshToken;

  @Indexed
  private final String accessToken;

  @Builder
  public RefreshToken(String userId, String refreshToken, String accessToken) {
    this.userId = userId;
    this.refreshToken = refreshToken;
    this.accessToken = accessToken;
  }
}
