package org.example.jwt;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/redis/")
public class RedisController {

  private static final String KEY = "transactions";

  private final StringRedisTemplate template;

  @PostMapping()
  public void addToSet(@RequestBody String transaction) {
    this.template.opsForSet().add(KEY, transaction);
  }

  @GetMapping()
  public Set<String> getKeyValues() {
    return this.template.opsForSet().members(KEY);
  }

}
