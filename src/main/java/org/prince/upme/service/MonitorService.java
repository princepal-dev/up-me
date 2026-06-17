package org.prince.upme.service;

import org.prince.upme.request.MonitorRequestDTO;
import org.prince.upme.response.ListMonitorDTO;
import org.prince.upme.response.MonitorDTO;

public interface MonitorService {
  MonitorDTO getMonitor(Long id);
  void updateStatus(Long id, boolean status);
  void deleteMonitor(Long id, String userName);
  ListMonitorDTO getAllMonitors(String userName);
  void updateMonitor(Long id, MonitorRequestDTO monitorRequestDTO);
  void createMonitor(MonitorRequestDTO monitorRequestDTO, String userName);
}
