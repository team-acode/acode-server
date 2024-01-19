package server.acode.domain.fragrance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.acode.domain.fragrance.dto.request.KeywordCond;
import server.acode.domain.fragrance.dto.response.ExtractResponse;
import server.acode.domain.fragrance.service.ExtractService;

@RestController
@RequestMapping("/api/v1/extract")
@RequiredArgsConstructor
@Tag(name = "Extract", description = "매칭 테스트 API")
public class ExtractController {
    private final ExtractService extractService;

    @Operation(summary = "매칭 테스트",
            description = "계열 최대 세 개, 추천 향수 최대 다섯 개(임의) 리턴됩니다\n\n" +
                    "concentration 1번 질문: 한 번 뿌릴 때, 향이 얼마나 지속되면 좋을까요? \n\n" +
                    " * 생각날 때마다 가볍게 뿌리고 싶다 1-2시간: `EDC`\n\n" +
                    " * 하루 두 세 번만 뿌리고 싶다 3-4시간: `EDT`\n\n" +
                    " * 한 번 뿌린 향이 하루종일 짙게 남아있으면 좋겠다: `EDP` \n\n" +
                    "season 2번 질문 어떤 계절에 뿌릴 향수를 찾으시나요? \n\n" +
                    "  `봄`  `여름`  `가을`  `겨울` \n\n" +
                    "mainFamily 3번 질문: 어떤 향이 주로 나면 좋을 것 같나요? \n\n" +
                    "  `나무/숲 향` `꽃/허브 향` `과일 향` `개성적인 향` \n\n" +
                    "scent 4번 질문: 어떤 향을 원하시나요? \n\n" +
                    "style 5번 질문: 향수로 어떤 분위기를 내고 싶으신가요? \n\n" +
                    "* scent랑 style은 둘 다 한글 키워드 넣으면 됩니당 \n\n" +
                    "* scent2랑 style2는 둘 다 키워드 두 개 선택된 경우 넣으면 됩니당 ")
    @PostMapping
    public ExtractResponse extractFamily(@RequestBody KeywordCond cond) {
        return extractService.extractFamily(cond);
    }
}
