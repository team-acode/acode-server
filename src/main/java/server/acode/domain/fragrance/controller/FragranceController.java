package server.acode.domain.fragrance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.acode.domain.fragrance.dto.response.GetFragranceNote;
import server.acode.domain.fragrance.dto.response.GetFragranceResponse;
import server.acode.domain.fragrance.service.FragranceService;
import server.acode.domain.user.entity.User;

@RestController
@RequestMapping("/api/v1/fragrance")
@RequiredArgsConstructor
public class FragranceController {
    private final FragranceService fragranceService;

    @GetMapping("/{fragranceId}")
    public GetFragranceResponse getFragranceDetail(
            @PathVariable("fragranceId") Long fragranceId,
            @AuthenticationPrincipal User user) {
        return fragranceService.getFragranceDetail(fragranceId, user);
    }

    @GetMapping("/{fragranceId}/note")
    public GetFragranceNote getFragranceNote(@PathVariable("fragranceId") Long fragranceId) {
        return fragranceService.getFragranceNote(fragranceId);
    }
}
