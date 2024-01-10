package server.acode.domain.family.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class FragranceByCatgegory {
    private Long fragranceId;
    private String fragranceName;
    private String brandName;
    private String style;
    private String poster;

    @QueryProjection
    public FragranceByCatgegory(Long fragranceId, String fragranceName, String brandName, String style, String poster){
        this.fragranceId = fragranceId;
        this.fragranceName = fragranceName;
        this.brandName = brandName;
        this.style = style;
        this.poster = poster;
    }
}
