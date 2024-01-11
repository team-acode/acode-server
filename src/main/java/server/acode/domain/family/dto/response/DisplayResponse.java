package server.acode.domain.family.dto.response;

import lombok.Data;

import java.util.List;
@Data
public class DisplayResponse {
    private int total;

    private List<DisplayFragrance> fragrances;

}
