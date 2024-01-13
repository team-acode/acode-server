package server.acode.domain.family.dto.response;

import lombok.Getter;
import server.acode.domain.family.entity.Family;

@Getter
public class DisplayFamily {
    private String korName;
    private String engName;
    private String keyword;
    private String summary;
    private String background;
    private String icon;

    private DisplayFamily(String korName, String engName, String keyword, String summary, String background, String icon){
        this.korName = korName;
        this.engName = engName;
        this.keyword = keyword;
        this.summary = summary;
        this.background = background;
        this.icon = icon;
    }

    public static DisplayFamily from(Family family){
        return new DisplayFamily(family.getKorName(),
                family.getEngName(),
                family.getKeyword(),
                family.getSummary(),
                family.getBackgroundImg(),
                family.getIcon());
    }
}
