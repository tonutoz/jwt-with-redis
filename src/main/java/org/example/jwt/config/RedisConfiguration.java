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

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "redis")
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

}
