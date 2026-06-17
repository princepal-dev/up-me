package org.prince.upme.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "monitors")
public class Monitor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull private String url;

  @NotNull
  @Size(max = 50)
  private String name;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "monitor")
  private List<PingLog> pingLog;

  private String status;

  private int baseIntervalMinutes = 10;

  private Instant nextPingAt;
  private Instant lastPingedAt;

  @UpdateTimestamp private LocalDateTime updatedAt;
  @CreationTimestamp private LocalDateTime createdAt;
}
