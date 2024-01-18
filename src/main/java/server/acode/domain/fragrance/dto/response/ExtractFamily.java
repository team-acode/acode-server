package server.acode.domain.fragrance.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
@Data
public class ExtractFamily {
    private String familyKorName;
    private String familyEngName;
    private String summary;
    private String icon;
    private List<String> keyword;


    @QueryProjection
    public ExtractFamily(String familyKorName, String familyEngName, String summary, String icon, String keyword){
        this.familyKorName = familyKorName;
        this.familyEngName = familyEngName;
        this.summary = summary;
        this.icon = icon;
        this.keyword = Arrays.asList(keyword.split(", "));
    }
}
