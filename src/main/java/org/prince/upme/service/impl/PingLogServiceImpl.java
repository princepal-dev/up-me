package org.prince.upme.service.impl;

import org.prince.upme.model.Monitor;
import org.prince.upme.model.PingLog;
import org.prince.upme.repository.MonitorRepository;
import org.prince.upme.repository.PingLogsRepository;
import org.prince.upme.response.PingListDTO;
import org.prince.upme.response.PingLogDTO;
import org.prince.upme.service.PingLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@Service
public class PingLogServiceImpl implements PingLogService {
  @Autowired private RestTemplate restTemplate;
  @Autowired private MonitorRepository monitorRepository;
  @Autowired private PingLogsRepository pingLogsRepository;

  private static final String[] USER_AGENTS = {
          "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
          "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36",
          "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36"
  };

  @Override
  public PingListDTO getAllLogs(Long monitorId, int pageNumber, int pageSize) {
    Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
    Page<PingLog> logsPage = pingLogsRepository.findByMonitorId(monitorId, pageDetails);
    List<PingLog> logsList = logsPage.getContent();

    List<PingLogDTO> logs =
        logsList.stream()
            .map(
                log -> {
                  PingLogDTO logDTO = new PingLogDTO();
                  logDTO.setSuccess(log.isSuccess());
                  logDTO.setStatusCode(log.getStatusCode());
                  logDTO.setResponseTimeMs(logDTO.getResponseTimeMs());
                  logDTO.setPingedAt(log.getPingedAt());
                  return logDTO;
                })
            .toList();

    PingListDTO response = new PingListDTO();
    response.setContent(logs);
    response.setPageNumber(logsPage.getNumber());
    response.setTotalElements(logsPage.getTotalElements());
    response.setPageSize(logsPage.getSize());
    response.setTotalPages(logsPage.getTotalPages());
    response.setLastPage(logsPage.isLast());

    return response;
  }

  @Scheduled(fixedRate = 60000)
  public void pingAllDueMonitors() {
    List<Monitor> due = monitorRepository.findMonitorsDueForPing();

    for (Monitor monitor : due) {
      pingMonitor(monitor);
      int jitterTime = monitor.getBaseIntervalMinutes() + new Random().nextInt(5) - 2;
      monitor.setLastPingedAt(Instant.now());
      monitor.setNextPingAt(Instant.now().plus(jitterTime, ChronoUnit.MINUTES));
      monitorRepository.save(monitor);
    }
  }

  private void pingMonitor(Monitor monitor) {
    long start = System.currentTimeMillis();

    PingLog log = new PingLog();
    log.setMonitor(monitor);
    log.setPingedAt(LocalDateTime.now());

    try {
      ResponseEntity<String> response = callUrl(monitor.getUrl());
      int statusCode = response.getStatusCode().value();
      log.setStatusCode(statusCode);
      log.setSuccess(statusCode >= 200 && statusCode < 300);
      log.setResponseTimeMs(System.currentTimeMillis() - start);
    } catch (Exception e) {
      log.setStatusCode(0);
      log.setSuccess(false);
      log.setResponseTimeMs(0L);
    }

    pingLogsRepository.save(log);
  }

  private ResponseEntity<String> callUrl(String url) {
    String randomAgent = USER_AGENTS[new Random().nextInt(USER_AGENTS.length)];

    HttpHeaders headers = new HttpHeaders();
    headers.set("User-Agent", randomAgent);

    HttpEntity<String> entity = new HttpEntity<>(headers);

    return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
  }
}
