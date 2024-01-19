package server.acode.domain.family.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
@Data
public class PageableResponse<T> {
    // TODO global로 옮겨야하나 고민

    private int totalPages;
    private long totalElements;

    private List<T> data;

    public PageableResponse(List<T> data, int totalPages, long totalElements){
        this.data = data;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public PageableResponse(Page<T> data) {
        this.totalElements = data.getTotalElements();
        this.totalPages = data.getTotalPages();
        this.data = data.getContent();
    }

}
