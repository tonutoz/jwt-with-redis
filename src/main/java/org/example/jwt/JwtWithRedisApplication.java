package org.example.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JwtWithRedisApplication {

  public static void main(String[] args) {
    SpringApplication.run(JwtWithRedisApplication.class, args);
  }

}
