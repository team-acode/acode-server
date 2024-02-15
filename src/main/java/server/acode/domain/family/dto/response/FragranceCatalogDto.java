package server.acode.domain.family.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import server.acode.domain.fragrance.entity.Concentration;

@Data
public class FragranceCatalogDto {

    private Long fragranceId;
    private String brandName; // 브랜드 이름
    private String fragranceName; // 향수 이름
    private String thumbnail;
    private String concentration;

    @QueryProjection
    public FragranceCatalogDto(Long fragranceId, String brandName, String fragranceName, String thumbnail, Concentration concentration){
        this.fragranceId = fragranceId;
        this.brandName = brandName;
        this.fragranceName = fragranceName;
        this.thumbnail = thumbnail;
        this.concentration = concentration.toString();
    }
}
