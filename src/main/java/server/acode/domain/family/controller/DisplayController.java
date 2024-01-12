package server.acode.domain.family.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.acode.domain.family.dto.request.FragranceFilterCond;
import server.acode.domain.family.dto.response.DisplayResponse;
import server.acode.domain.family.service.DisplayService;
import server.acode.global.auth.security.CustomUserDetails;
import server.acode.global.common.PageRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "계열 상세/ 브랜드 상세/ 추천향료 상세", description = "계열 상세/ 브랜드 상세/ 추천향료 상세 API")
public class DisplayController {

    private final DisplayService displayService;

    @Operation(summary = "계열별/ 브랜드별 향수리스트",
            description = "필요한 값만 파라미터에 넣으면 됩니다" +
                    "계열 두 개 검색 시에는 두 계열 사이 공백 한 칸 넣어주세요 url 상으로는 %20으로 나타난다합니다")
    @GetMapping("/display")
    public DisplayResponse displayV1(FragranceFilterCond cond, PageRequest pageRequest){
        return displayService.searchFragranceList(cond, pageRequest);
    }


    @GetMapping("/user")
    public void test(){
        UserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user.getUsername());
    }
}
