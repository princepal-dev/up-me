package org.prince.upme.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.prince.upme.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
  @Serial private static final long serialVersionUID = 1L;

  private Long id;
  private String email;
  private String userName;

  @JsonIgnore private String password;

  public UserDetailsImpl(Long id, String email, String userName, String password) {
    this.id = id;
    this.email = email;
    this.userName = userName;
    this.password = password;
  }

  public static UserDetailsImpl build(User user) {
    return new UserDetailsImpl(
        user.getId(), user.getEmail(), user.getUserName(), user.getPassword());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public @Nullable String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return userName;
  }
}
