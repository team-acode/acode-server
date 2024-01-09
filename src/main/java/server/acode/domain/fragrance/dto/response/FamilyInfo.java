package server.acode.domain.fragrance.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.domain.family.entity.Family;

@Getter
@NoArgsConstructor
public class FamilyInfo {
    private Long familyId;
    private String familyIcon;
    private String familyName;

    @Builder
    public FamilyInfo(Family family) {
        this.familyId = family.getId();
        this.familyIcon = family.getIcon();
        this.familyName = family.getKorName();
    }
}
