package server.acode.domain.fragrance.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class BrandInfo {
    private Long brandId;
    private String korName;
    private String roundImg;

    @QueryProjection
    public BrandInfo(Long brandId, String korName, String roundImg) {
        this.brandId = brandId;
        this.korName = korName;
        this.roundImg = roundImg;
    }
}
