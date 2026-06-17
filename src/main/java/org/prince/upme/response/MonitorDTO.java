package org.prince.upme.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MonitorDTO {
    private Long id;
    private String url;
    private String name;
    private String isActive;
    private LocalDateTime createdAt;
}
