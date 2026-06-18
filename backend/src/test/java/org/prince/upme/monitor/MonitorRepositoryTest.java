package org.prince.upme.monitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.prince.upme.model.Monitor;
import org.prince.upme.repository.MonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

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
    monitor.setLastPingedAt(Instant.now().minus(10, ChronoUnit.MINUTES));
    underTest.save(monitor);

    // when
    Monitor foundMonitor = underTest.findMonitorsDueForPing().get(0);

    // then
    assertEquals(monitor.getId(), foundMonitor.getId());
  }
}
