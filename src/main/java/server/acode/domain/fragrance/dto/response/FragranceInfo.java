package server.acode.domain.fragrance.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class FragranceInfo {
    private Long fragranceId;
    private String thumbnail;
    private String fragranceName;
    private String korBrand;

    @QueryProjection
    public FragranceInfo(Long fragranceId, String thumbnail, String fragranceName, String korBrand) {
        this.fragranceId = fragranceId;
        this.thumbnail = thumbnail;
        this.fragranceName = fragranceName;
        this.korBrand = korBrand;
    }
}
