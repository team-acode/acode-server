package server.acode.domain.fragrance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetFragranceReviewStatistics {
    private List<ReviewStatInfo> seasonStat;
    private List<ReviewStatInfo> longevityStat;
    private List<ReviewStatInfo> intensityStat;
    private List<ReviewStatInfo> styleStat;
}
