package server.acode.domain.family.dto.response;

import lombok.Getter;
import server.acode.domain.fragrance.entity.Brand;
@Getter
public class DisplayBrand {
    private String korName;
    private String engName;
    private String keyword;
    private String summary;

    private DisplayBrand(String korName, String engName, String keyword, String summary){
        this.korName = korName;
        this.engName = engName;
        this.keyword = keyword;
        this.summary = summary;
    }

    public static DisplayBrand from(Brand brand){
        return new DisplayBrand(brand.getKorName(),
                brand.getEngName(),
                brand.getKeyword(),
                brand.getSummary());
    }
}
