package org.example.jwt.account;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinRequest {

  @NotNull
  @NotBlank
  private String userId;

  @NotNull
  private String password;

  @NotNull
  private String nickname;

  @Email
  private String eMail;

  @Builder
  public JoinRequest(String userId, String password, String nickname, String eMail) {
    this.userId = userId;
    this.password = password;
    this.nickname = nickname;
    this.eMail = eMail;
  }
}
