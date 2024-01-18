package server.acode.domain.fragrance.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class FamilyCountDto {
    private Long familyId;
    private Integer count;

    @QueryProjection
    public FamilyCountDto(Long familyId, Integer count) {
        this.familyId = familyId;
        this.count = count;
    }
}
