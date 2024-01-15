package org.example.jwt.account;

import java.util.Optional;
import org.example.jwt.account.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

  public Optional<User> findByUserId(final String userId);

}
