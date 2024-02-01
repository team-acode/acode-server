package server.acode.domain.fragrance.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import server.acode.domain.fragrance.entity.Concentration;

@Data
public class FragranceInfo {
    private Long fragranceId;
    private String thumbnail;
    private String fragranceName;
    private String brandName;
    private String concentration;

    @QueryProjection
    public FragranceInfo(Long fragranceId, String thumbnail, String fragranceName, String korBrand, Concentration concentration) {
        this.fragranceId = fragranceId;
        this.thumbnail = thumbnail;
        this.fragranceName = fragranceName;
        this.brandName = korBrand;
        this.concentration = concentration.toString();
    }
}
