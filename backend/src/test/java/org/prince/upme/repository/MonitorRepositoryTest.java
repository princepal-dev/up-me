package org.prince.upme.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.prince.upme.model.Monitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MonitorRepositoryTest {
    @Autowired
    private MonitorRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindMonitorsDueForPing() {
        // given
        Monitor monitor = new Monitor();
        monitor.setUrl("exm.com");
        monitor.setName("test");
        monitor.setLastPingedAt(Instant.now().minus(10, ChronoUnit.MINUTES));
        underTest.save(monitor);

        // when
        List<Monitor> monitors = underTest.findMonitorsDueForPing();

        // then
        assertThat(monitors).hasSize(1);
        assertThat(monitors.getFirst().getId()).isEqualTo(monitor.getId());
    }
}
