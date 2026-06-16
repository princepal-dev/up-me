package org.prince.upme.repository;

import org.prince.upme.model.PingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PingLogsRepository extends JpaRepository<PingLog, Long> {}
