package server.acode.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.List;

@Getter
@NoArgsConstructor
public class PreviewUserInfo {
    private String nickname;
    private int reviewCnt;
    private List<PreviewScrap> scraps;

    @Builder
    public PreviewUserInfo(String nickname, int reviewCnt, List<PreviewScrap> scraps){
        this.nickname = nickname;
        this.reviewCnt = reviewCnt;
        this.scraps = scraps;
    }

}
