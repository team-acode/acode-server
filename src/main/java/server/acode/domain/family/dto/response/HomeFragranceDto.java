package server.acode.domain.family.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class HomeFragranceDto {
    private Long fragranceId;
    private String fragranceName;
    private String brandName;
    private List<String> style;
    private String poster;

    @QueryProjection
    public HomeFragranceDto(Long fragranceId, String fragranceName, String brandName, String style, String poster){
        this.fragranceId = fragranceId;
        this.fragranceName = fragranceName;
        this.brandName = brandName;
        this.style = Arrays.asList(style.split(", "))
                .subList(0, Math.min(style.split(", ").length, 3));
        this.poster = poster;
    }
}
