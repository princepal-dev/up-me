package org.prince.upme.response;

import lombok.Data;

import java.util.List;

@Data
public class PingListDTO {
    private int pageSize;
    private int pageNumber;
    private int totalPages;
    private boolean lastPage;
    private Long totalElements;
    private List<PingLogDTO> content;
}
