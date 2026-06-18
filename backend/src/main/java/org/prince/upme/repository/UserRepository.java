package org.prince.upme.repository;

import java.util.Optional;

import org.prince.upme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  Optional<User> findByUserName(String userName);

  boolean existsByUserName(@NotBlank @Size(max = 50) String userName);
}
