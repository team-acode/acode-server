package server.acode.domain.fragrance.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.domain.fragrance.entity.Fragrance;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetFragranceResponse {
    private Long fragranceId;
    private boolean isScraped;
    private String thumbnail;
    private String image1;
    private String image2;

    private String brandName;
    private String fragranceName;
    private String concentration;

    private List<FamilyInfo> familyList;
    private List<CapacityInfo> capacityList;
    private List<String> style;

    public GetFragranceResponse(Fragrance fragrance, boolean isScraped, List<FamilyInfo> familyList, List<CapacityInfo> capacityList) {
        this.fragranceId = fragrance.getId();
        this.thumbnail = fragrance.getThumbnail();
        this.image1 = fragrance.getImage1();
        this.image2 = fragrance.getImage2();
        this.brandName = fragrance.getBrand().getKorName();
        this.fragranceName = fragrance.getName();
        this.concentration = fragrance.getConcentration().name();
        this.style = Arrays.asList(fragrance.getStyle().split(", "));

        this.isScraped = isScraped;
        this.familyList = familyList;
        this.capacityList = capacityList;
    }
}
