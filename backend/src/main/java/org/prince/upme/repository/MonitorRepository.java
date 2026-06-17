package org.prince.upme.repository;

import org.prince.upme.model.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Long> {
  @Query(value = "SELECT * from monitor where last_pinged_at <= now()", nativeQuery = true)
  List<Monitor> findMonitorsDueForPing();
}
