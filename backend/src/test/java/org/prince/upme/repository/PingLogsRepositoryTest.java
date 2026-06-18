package org.prince.upme.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.prince.upme.model.Monitor;
import org.prince.upme.model.PingLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PingLogsRepositoryTest {
    @Autowired
    private PingLogsRepository underTest;

    @Autowired
    private MonitorRepository monitorRepository;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        monitorRepository.deleteAll();
    }

    @Test
    void findByMonitorId() {
        // given
        Monitor monitor = new Monitor();
        monitor.setUrl("example.com");
        monitor.setName("test");

        monitorRepository.saveAndFlush(monitor);

        PingLog log = new PingLog();
        log.setMonitor(monitor);

        underTest.saveAndFlush(log);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<PingLog> pages = underTest.findByMonitorId(monitor.getId(), pageable);

        // then
        assertThat(pages.getContent()).hasSize(1);
        assertThat(pages.getContent().getFirst().getMonitor().getId()).isEqualTo(monitor.getId());
    }
}