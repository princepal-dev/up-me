package org.prince.upme.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(
    uniqueConstraints = {
      @UniqueConstraint(columnNames = "email"),
      @UniqueConstraint(columnNames = "username")
    })
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Size(max = 50)
  private String email;

  @NotNull
  @Size(max = 50)
  private String userName;

  @NotNull private String signUpMethod;

  public User(String email, String userName, String signUpMethod, String password) {
    this.email = email;
    this.userName = userName;
    this.signUpMethod = signUpMethod;
    this.password = password;
  }

  @NotNull
  @Size(min = 7, max = 50)
  private String password;

  @OneToMany(mappedBy = "user")
  private List<Monitor> monitors;

  @CreationTimestamp private LocalDateTime createdAt;

  @UpdateTimestamp private LocalDateTime updatedAt;
}
