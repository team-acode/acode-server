package server.acode.domain.family.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.acode.domain.family.dto.response.HomeFragrance;
import server.acode.domain.family.service.HomeService;
import server.acode.domain.ingredient.dto.response.IngredientOfTheDay;
import server.acode.global.auth.security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "home", description = "홈 API")
public class HomeController {

    private final HomeService homeService;

    @Operation(summary = "계열별 향수 최대 6개",
            description = "아직 포스터 이미지가 준비되지 않아 우디에만 테스트용으로 넣어놨습니다 우디로 테스트 해주세요")
    @GetMapping("/home")
    public List<HomeFragrance> searchV1(@RequestParam String family){
        return homeService.search(family);
    }

    @Operation(summary = "오늘의 추천 향료")
    @GetMapping("/home/recommend")
    public IngredientOfTheDay recommendV1(){
        return homeService.recommendIngredient();
    }

    @GetMapping("/test/user")
    @Operation(summary = "유저 확인 개발 테스트용")
    public void test(){
        UserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user.getUsername());
    }

}
