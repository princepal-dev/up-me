package org.prince.upme.controller;

import org.prince.upme.request.MonitorRequestDTO;
import org.prince.upme.response.ListMonitorDTO;
import org.prince.upme.response.MonitorDTO;
import org.prince.upme.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @GetMapping("/")
    public ResponseEntity<ListMonitorDTO> getAllMonitors(
            @AuthenticationPrincipal Authentication authentication) {
        String userName = authentication.getName();
        ListMonitorDTO response = monitorService.getAllMonitors(userName);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonitorDTO> getMonitor(@PathVariable Long id) {
        MonitorDTO response = monitorService.getMonitor(id);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateMonitor(@RequestBody MonitorRequestDTO monitorRequestDTO, @PathVariable Long id) {
        monitorService.updateMonitor(id, monitorRequestDTO);
        return ResponseEntity.ok().body("Monitor updated successfully!");
    }

    @PostMapping("/")
    public ResponseEntity<String> createMonitor(
            @RequestBody MonitorRequestDTO monitorRequestDTO,
            @AuthenticationPrincipal Authentication authentication) {
        String userName = authentication.getName();
        monitorService.createMonitor(monitorRequestDTO, userName);
        return ResponseEntity.ok().body("Monitor created successfully!");
    }

    @PutMapping("/update-active/{id}")
    public ResponseEntity<String> updateMonitorStatus(
            @PathVariable Long id, @RequestParam String status) {
        monitorService.updateStatus(id, status);
        return ResponseEntity.ok().body("Monitor status updated successfully!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMonitor(
            @PathVariable Long id, @AuthenticationPrincipal Authentication authentication) {
        String userName = authentication.getName();
        monitorService.deleteMonitor(id, userName);
        return ResponseEntity.ok().body("Monitor deleted successfully!");
    }
}
