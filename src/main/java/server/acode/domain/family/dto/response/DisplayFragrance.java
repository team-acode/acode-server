package server.acode.domain.family.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class DisplayFragrance {

    private Long fragranceId;
    private String brandName; // 브랜드 이름
    private String fragranceName; // 향수 이름
    private String thumbnail;

    @QueryProjection
    public DisplayFragrance (Long fragranceId, String brandName, String fragranceName, String thumbnail){
        this.fragranceId = fragranceId;
        this.brandName = brandName;
        this.fragranceName = fragranceName;
        this.thumbnail = thumbnail;
    }
}
