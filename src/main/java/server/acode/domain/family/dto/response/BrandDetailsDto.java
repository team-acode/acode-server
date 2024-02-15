package server.acode.domain.family.dto.response;

import lombok.Getter;
import server.acode.domain.fragrance.entity.Brand;

@Getter
public class BrandDetailsDto {
    private String korName;
    private String engName;
    private String summary;
    private String background;

    private BrandDetailsDto(String korName, String engName, String summary, String background){
        this.korName = korName;
        this.engName = engName;
        this.summary = summary;
        this.background = background;
    }

    public static BrandDetailsDto from(Brand brand){
        return new BrandDetailsDto(brand.getKorName(),
                brand.getEngName(),
                brand.getSummary(),
                brand.getBackgroundImg());
    }
}
