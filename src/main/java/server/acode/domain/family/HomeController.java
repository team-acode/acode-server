package server.acode.domain.family;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "posts", description = "게시물 API")
@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {

    @Operation(summary = "home/향수리스트", description = "home 화면에서 향수 리스트 가져오기")
    @Parameters({
            @Parameter(name = "family", description = "향료", example = "우디")
    })
    @GetMapping("/fragrances")
    public void getFragrances(){

    }
}
