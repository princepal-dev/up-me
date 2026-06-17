package org.prince.upme.response;

import lombok.Data;

import java.util.List;

@Data
public class ListMonitorDTO {
    private int count;
    private List<MonitorDTO> monitors;
}
