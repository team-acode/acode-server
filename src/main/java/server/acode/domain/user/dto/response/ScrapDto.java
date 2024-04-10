package server.acode.domain.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.domain.fragrance.entity.Concentration;

@Getter
@NoArgsConstructor
public class ScrapDto {
    private Long fragranceId;
    private String fragranceName;
    private String brandName;
    private String concentration;
    private String thumbnail;


    @QueryProjection
    public ScrapDto(Long fragranceId, String fragranceName, String brandName, Concentration concentration, String thumbnail){
        this.fragranceId = fragranceId;
        this.fragranceName = fragranceName;
        this.brandName = brandName;
        this.concentration = concentration.toString();
        this.thumbnail = thumbnail;
    }

}
