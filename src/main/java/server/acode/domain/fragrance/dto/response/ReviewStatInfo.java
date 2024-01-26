package server.acode.domain.fragrance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewStatInfo {
    private String keyword;
    private int percentage;

    public ReviewStatInfo(Map.Entry<String, Integer> entry) {
        this.keyword = entry.getKey();
        this.percentage = entry.getValue();
    }
}
