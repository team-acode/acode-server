package server.acode.domain.family.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
@Data
public class PageableResponse<T> {
    // TODO global로 옮겨야하나 고민

    private List<T> data;

    private int totalPages;
    private long totalElements;

    public PageableResponse(List<T> data, int totalPages, long totalElements){
        this.data = data;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

}
