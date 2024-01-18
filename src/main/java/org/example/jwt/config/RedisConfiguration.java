package org.example.jwt.config;

import io.lettuce.core.ReadFrom;
import java.util.List;
import java.util.Properties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "redis")
@EnableRedisRepositories
public class RedisConfiguration {

  private RedisProperties master;
  private List<RedisReplicaProperties> replicas;

  /**
   * Lettuce
   */
  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
        .readFrom(ReadFrom.REPLICA_PREFERRED)
        .build();
    final RedisStaticMasterReplicaConfiguration staticMasterReplicaConfiguration = new RedisStaticMasterReplicaConfiguration(master.getHost(), master.getPort());
    getReplicas().forEach(replica -> staticMasterReplicaConfiguration.addNode(replica.getHost(), replica.getPort()));
    return new LettuceConnectionFactory(staticMasterReplicaConfiguration, clientConfig);
  }

  @Getter
  @Setter
  private static class RedisProperties {

    private String host;
    private int port;
  }

  @Getter
  @Setter
  private static class RedisReplicaProperties {

    private String host;
    private int port;
  }

  @Bean
  public RedisTemplate<String, String> redisTemplate() {
    // redisTemplate를 받아와서 set, get, delete를 사용
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    // setKeySerializer, setValueSerializer 설정
    // redis-cli을 통해 직접 데이터를 조회 시 알아볼 수 없는 형태로 출력되는 것을 방지
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());
    redisTemplate.setConnectionFactory(redisConnectionFactory());

    return redisTemplate;
  }

}
