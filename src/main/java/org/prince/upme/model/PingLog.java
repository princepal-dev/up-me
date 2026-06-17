package org.prince.upme.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
public class PingLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ping-log")
  private Monitor monitor;

  private int statusCode;

  private boolean success;

  @CreationTimestamp private LocalDateTime pingedAt;
}
