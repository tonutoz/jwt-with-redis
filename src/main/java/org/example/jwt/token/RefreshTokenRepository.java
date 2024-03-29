package org.example.jwt.token;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken,String> {
  Optional<RefreshToken> findByRefreshToken(String refreshToken);

}
