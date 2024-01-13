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
import server.acode.domain.user.entity.User;
import server.acode.domain.user.repository.UserRepository;
import server.acode.global.auth.security.CustomUserDetails;
import server.acode.global.common.ErrorCode;
import server.acode.global.exception.CustomException;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "home", description = "홈 API")
public class HomeController {

    private final HomeService homeService;
    private final UserRepository userRepository;

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
    @Operation(summary = "유저 확인 테스트용", description = "개발자용입니다 토큰 넣고 호출 시 사용자 이름이 리턴됩니다")
    public String test(){
        UserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User byAuthKey = userRepository.findByAuthKey(user.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return byAuthKey.getNickname();
    }


}
