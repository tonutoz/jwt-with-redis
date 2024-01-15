package org.example.jwt.account;

import java.time.LocalDateTime;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "`user`")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreatedDate
  @Column(name = "created_on")
  private LocalDateTime createdOn;

  @LastModifiedDate
  @Column(name = "modified_on")
  private LocalDateTime modifiedOn;

  @Column(name = "user_id", length = 50, unique = true)
  private String userId;

  @Column(name = "password", length = 256)
  private String password;

  @Column(name = "nickname", length = 50)
  private String nickname;

  @Column(name ="e-mail", length = 255)
  private String email;

  @Column(name = "activated")
  @ColumnDefault("true")
  private boolean activated;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "user_roll")
  private UserRole userRole;

  @Builder
  public User(String userId,
      String password, String nickname,String email, UserRole userRole) {
    this.userId = userId;
    this.password = password;
    this.nickname = nickname;
    this.email = email;
    this.userRole = userRole;
  }

}
