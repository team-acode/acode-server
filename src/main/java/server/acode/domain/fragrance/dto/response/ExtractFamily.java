package server.acode.domain.fragrance.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import server.acode.domain.family.entity.Family;

import java.util.Arrays;
import java.util.List;
@Data
public class ExtractFamily {
    private String familyKorName;
    private String familyEngName;
    private String summary;
    private String icon;
    private List<String> keyword;


    public ExtractFamily(String familyKorName, String familyEngName, String summary, String icon, String keyword){
        this.familyKorName = familyKorName;
        this.familyEngName = familyEngName;
        this.summary = summary;
        this.icon = icon;
        this.keyword = Arrays.asList(keyword.split(", "));
    }

    public static ExtractFamily from(Family family){
        return new ExtractFamily(family.getKorName(), family.getEngName(), family.getSummary(),
                family.getIcon(), family.getKeyword());
    }
}
