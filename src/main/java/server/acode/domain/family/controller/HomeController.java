package server.acode.domain.family.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.acode.domain.family.dto.request.FragranceSearchCond;
import server.acode.domain.family.dto.response.FragranceByCatgegory;
import server.acode.domain.family.service.HomeService;
import server.acode.domain.ingredient.dto.response.IngredientOfTheDay;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "home", description = "홈 API")
public class HomeController {

    private final HomeService homeService;

    @Operation(description = "계열별 향수 최대 6개")
    @GetMapping("/home")
    public List<FragranceByCatgegory> searchV1(FragranceSearchCond condition){
        return homeService.search(condition);
    }

    @Operation(description = "오늘의 추천 향료")
    @GetMapping("/home/recommend")
    public IngredientOfTheDay recommendV1(){
        return homeService.recommendIngredient();
    }

}
