package server.acode.domain.fragrance.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class ExtractFragrance {
    private Long fragranceId;
    private String fragranceName;
    private String brandName;
    private String familyName;
    private String thumbnail;

    @QueryProjection
    public ExtractFragrance(Long fragranceId, String fragranceName, String  brandName, String familyName, String thumbnail){
        this.fragranceId =fragranceId;
        this.fragranceName =fragranceName;
        this.brandName = brandName;
        this.familyName = familyName;
        this.thumbnail = thumbnail;
    }
}
