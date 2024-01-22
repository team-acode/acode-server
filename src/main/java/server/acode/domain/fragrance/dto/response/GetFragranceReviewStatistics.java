package server.acode.domain.fragrance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetFragranceReviewStatistics {
    private int reviewCnt;

    private String season;
    private int seasonMax;

    private String longevity;
    private int longevityMax;

    private String intensity;
    private int intensityMax;

    private String style1;
    private int styleMax1;

    private String style2;
    private int styleMax2;

    private String style3;
    private int styleMax3;
}
