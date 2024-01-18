package server.acode.domain.fragrance.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public class SearchBrandResponse {
    private List<BrandInfo> brandList;

    public SearchBrandResponse(List<BrandInfo> brandList) {
        this.brandList = brandList;
    }
}
