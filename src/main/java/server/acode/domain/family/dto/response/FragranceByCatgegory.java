package server.acode.domain.family.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class FragranceByCatgegory {
    private String fragranceName;
    private String brandName;
    private String style;

    @QueryProjection
    public FragranceByCatgegory(String fragranceName, String brandName, String style){
        this.fragranceName = fragranceName;
        this.brandName = brandName;
        this.style = style;
    }
}
