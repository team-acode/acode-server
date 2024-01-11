package server.acode.domain.family.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.acode.domain.family.dto.request.FragranceCategoryCond;
import server.acode.domain.family.dto.response.DisplayFragrance;
import server.acode.domain.family.dto.response.DisplayResponse;
import server.acode.domain.family.service.DisplayService;
import server.acode.domain.user.entity.User;
import server.acode.global.auth.security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "계열 상세/ 브랜드 상세/ 추천향료 상세", description = "계열 상세/ 브랜드 상세/ 추천향료 상세 API")
public class DisplayController {

    private final DisplayService displayService;

    @Operation(description = "계열별/ 브랜드별 향수리스트")
    @GetMapping("/display")
    public Page<DisplayFragrance> displayV1(FragranceCategoryCond cond, Pageable pageable){
        return displayService.searchFragranceList(cond, pageable);
    }
    @GetMapping("/user")
    public void test(){
        UserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user.getUsername());
    }
}
