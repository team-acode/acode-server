package server.acode.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.List;

@Getter
@NoArgsConstructor
public class UserBasicInfoDto {
    private String nickname;
    private int reviewCnt;
    private List<ScrapPreviewDto> scraps;

    @Builder
    public UserBasicInfoDto(String nickname, int reviewCnt, List<ScrapPreviewDto> scraps){
        this.nickname = nickname;
        this.reviewCnt = reviewCnt;
        this.scraps = scraps;
    }

}
