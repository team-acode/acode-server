package server.acode.domain.fragrance.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetFragranceSimilar {
    private List<FragranceInfo> similarFragranceList;

    public GetFragranceSimilar(List<FragranceInfo> similarFragranceList) {
        this.similarFragranceList = similarFragranceList;
    }
}
