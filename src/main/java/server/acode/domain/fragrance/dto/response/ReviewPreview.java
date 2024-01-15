package server.acode.domain.fragrance.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ReviewPreview {
    private String comment;
    private int rate;
    private String thumbnail;
    private String nickname;

    @QueryProjection
    public ReviewPreview(String comment, int rate, String thumbnail, String nickname) {
        this.comment = comment;
        this.rate = rate;
        this.thumbnail = thumbnail;
        this.nickname = nickname;
    }
}
