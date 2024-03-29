package server.acode.domain.fragrance.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class BrandInfo {
    private Long brandId;
    private String brandName;
    private String roundImg;

    @QueryProjection
    public BrandInfo(Long brandId, String brandName, String roundImg) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.roundImg = roundImg;
    }
}
