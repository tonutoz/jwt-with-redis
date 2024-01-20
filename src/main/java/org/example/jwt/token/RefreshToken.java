package org.example.jwt.token;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@ToString
@Getter
//@RedisHash(value = "jwtToken", timeToLive = 60*60*24*3)
@RedisHash(value = "jwtToken", timeToLive =1000*60*3)
public class RefreshToken {

  @Id
  private String userId;

  @Indexed
  private final String refreshToken;

  @Builder
  public RefreshToken(String userId, String refreshToken) {
    this.userId = userId;
    this.refreshToken = refreshToken;
  }

}
