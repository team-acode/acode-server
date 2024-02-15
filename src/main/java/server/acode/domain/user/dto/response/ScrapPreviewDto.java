package server.acode.domain.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScrapPreviewDto {
    private Long fragranceId;
    private String thumbnail;

    @QueryProjection
    public ScrapPreviewDto(Long fragranceId, String thumbnail){
        this.fragranceId = fragranceId;
        this.thumbnail = thumbnail;
    }
}
