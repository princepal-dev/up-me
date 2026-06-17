package org.prince.upme.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PingLogDTO {
    private int statusCode;
    private boolean success;
    private long responseTimeMs;
    private LocalDateTime pingedAt;
}
