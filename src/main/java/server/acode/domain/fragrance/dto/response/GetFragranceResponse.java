package server.acode.domain.fragrance.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.domain.fragrance.entity.Fragrance;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetFragranceResponse {
    private Long fragranceId;
    private boolean isScraped;
    private String thumbnail;
    private String image1;
    private String image2;

    private String korBrand;
    private String fragranceName;
    private String concentration;

    private List<FamilyInfo> familyList;
    private List<CapacityInfo> capacityList;
    private String style;

//    @Builder
    public GetFragranceResponse(Fragrance fragrance, boolean isScraped, List<FamilyInfo> familyList, List<CapacityInfo> capacityList) {
        this.fragranceId = fragrance.getId();
        this.thumbnail = fragrance.getThumbnail();
        this.image1 = fragrance.getImage1();
        this.image2 = fragrance.getImage2();
        this.korBrand = fragrance.getKorBrand();
        this.fragranceName = fragrance.getName();
        this.concentration = fragrance.getConcentration().name();
        this.style = fragrance.getStyle();

        this.isScraped = isScraped;
        this.familyList = familyList;
        this.capacityList = capacityList;
    }
}
