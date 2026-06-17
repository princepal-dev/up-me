package org.prince.upme.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.prince.upme.model.Monitor;
import org.prince.upme.model.User;
import org.prince.upme.repository.MonitorRepository;
import org.prince.upme.repository.UserRepository;
import org.prince.upme.request.MonitorRequestDTO;
import org.prince.upme.response.ListMonitorDTO;
import org.prince.upme.response.MonitorDTO;
import org.prince.upme.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MonitorServiceImpl implements MonitorService {
  @Value("${upme.user.monitor.max}")
  private int maxMonitorAllowed;

  @Autowired private UserRepository userRepository;
  @Autowired private MonitorRepository monitorRepository;

  @Override
  public void updateStatus(Long id, boolean status) {
    Monitor monitor =
        monitorRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Unable to find monitor with id : " + id));

    monitor.setStatus(status);
    monitorRepository.save(monitor);
  }

  @Override
  @Transactional
  public void deleteMonitor(Long id, String userName) {
    Monitor monitor =
        monitorRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Unable to find monitor with id : " + id));

    User user = monitor.getUser();

    if (!user.getUserName().equals(userName))
      throw new RuntimeException("You are allowed to access this resource!");

    monitorRepository.delete(monitor);
    user.setMonitorCount(user.getMonitorCount() - 1);
    userRepository.save(user);
  }

  @Override
  public ListMonitorDTO getAllMonitors(String userName) {
    User user =
        userRepository
            .findByUserName(userName)
            .orElseThrow(
                () -> new UsernameNotFoundException("Unable to find username : " + userName));

    List<Monitor> monitors = user.getMonitors();
    List<MonitorDTO> monitorDTOS = monitors.stream().map(this::getMonitorDTO).toList();
    ListMonitorDTO response = new ListMonitorDTO();

    response.setCount(monitors.size());
    response.setMonitors(monitorDTOS);
    return response;
  }

  @Override
  public void updateMonitor(Long id, MonitorRequestDTO monitorRequestDTO) {
    Monitor monitor =
            monitorRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("Unable to find monitor with id : " + id));
    monitor.setName(monitorRequestDTO.getName());
    monitor.setUrl(monitorRequestDTO.getUrl());

    monitorRepository.save(monitor);
  }

  @Override
  public MonitorDTO getMonitor(Long id) {
    Monitor monitor =
        monitorRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Unable to find monitor with id : " + id));

    return getMonitorDTO(monitor);
  }

  @NonNull
  private MonitorDTO getMonitorDTO(Monitor monitor) {
    MonitorDTO response = new MonitorDTO();
    response.setId(monitor.getId());
    response.setUrl(monitor.getUrl());
    response.setName(monitor.getName());
    response.setActive(monitor.isStatus());
    response.setCreatedAt(monitor.getCreatedAt());
    return response;
  }

  @Override
  @Transactional
  public void createMonitor(MonitorRequestDTO monitorRequestDTO, String userName) {
    User user =
        userRepository
            .findByUserName(userName)
            .orElseThrow(
                () -> new UsernameNotFoundException("Unable to find username : " + userName));

    if (user.getMonitorCount() >= maxMonitorAllowed)
      throw new RuntimeException("Max Monitors are created by the user, kindly delete");

    Monitor monitor = new Monitor();
    monitor.setUrl(monitorRequestDTO.getUrl());
    monitor.setName(monitorRequestDTO.getName());
    monitor.setStatus(true);

    user.setMonitorCount(user.getMonitorCount() + 1);
    userRepository.save(user);
    monitorRepository.save(monitor);
  }
}
