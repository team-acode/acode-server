package server.acode.domain.family.dto;

import lombok.Data;
import server.acode.domain.fragrance.dto.response.FragranceInfo;

import java.util.List;

@Data
public class SimilarFragranceOrCond {
    private List<Long> selectedFragranceIdList;
    private Long familyId1;
    private Long familyId2;
    private int count;

    public SimilarFragranceOrCond(List<Long> selectedFragranceIdList, Long familyId1, Long familyId2, int count) {
        this.selectedFragranceIdList = selectedFragranceIdList;
        this.familyId1 = familyId1;
        this.familyId2 = familyId2;
        this.count = count;
    }

    public static SimilarFragranceOrCond from(List<Long> familyIdList, List<Long> selectedFragranceIdList) {
        return new SimilarFragranceOrCond(
                selectedFragranceIdList,
                familyIdList.get(0), familyIdList.get(1),
                6 - selectedFragranceIdList.size());
    }
}
