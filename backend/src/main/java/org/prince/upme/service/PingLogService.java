package org.prince.upme.service;

import org.prince.upme.response.PingListDTO;

public interface PingLogService {
    PingListDTO getAllLogs(Long monitorId, int pageNumber, int pageSize);
}
