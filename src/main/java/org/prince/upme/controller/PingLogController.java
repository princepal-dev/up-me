package org.prince.upme.controller;

import org.prince.upme.config.AppConstants;
import org.prince.upme.response.PingListDTO;
import org.prince.upme.service.PingLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ping")
public class PingLogController {
  @Autowired private PingLogService pingLogService;

  @GetMapping("/{id}")
  public ResponseEntity<PingListDTO> getAllPingLogs(
      @PathVariable Long id,
      @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false)
          int pageNumber,
      @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false)
          int pageSize) {
    PingListDTO response = pingLogService.getAllLogs(id, pageNumber, pageSize);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
