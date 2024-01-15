package server.acode.domain.fragrance.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.acode.domain.fragrance.entity.Fragrance;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetFragrancePurchase {
    private String link1; // 구매 링크 url
    private String link2;
    private String link3;

    public GetFragrancePurchase(String link1, String link2, String link3) {
        this.link1 = link1;
        this.link2 = link2;
        this.link3 = link3;
    }

    public static GetFragrancePurchase from(Fragrance fragrance) {
        return new GetFragrancePurchase(
                fragrance.getLink1(), fragrance.getLink2(), fragrance.getLink3()
        );
    }
}
