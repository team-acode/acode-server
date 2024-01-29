package server.acode.domain.family.dto.response;

import lombok.Getter;
import server.acode.domain.fragrance.entity.Brand;

import java.util.Arrays;
import java.util.List;

@Getter
public class DisplayBrand {
    private String korName;
    private String engName;
    private String summary;
    private String background;

    private DisplayBrand(String korName, String engName, String summary, String background){
        this.korName = korName;
        this.engName = engName;
        this.summary = summary;
        this.background = background;
    }

    public static DisplayBrand from(Brand brand){
        return new DisplayBrand(brand.getKorName(),
                brand.getEngName(),
                brand.getSummary(),
                brand.getBackgroundImg());
    }
}
