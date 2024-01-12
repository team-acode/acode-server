package server.acode.domain.family.dto.response;

import lombok.Data;

import java.util.List;
@Data
public class DisplayResponse {

    private List<DisplayFragrance> data;

    private int totalPages;
    private long totalElements;

}
