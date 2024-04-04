package server.acode.domain.fragrance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.acode.domain.fragrance.dto.response.GetFragranceResponse;
import server.acode.global.auth.security.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class SynchronizedFragranceService {
    private final FragranceService fragranceService;

    public synchronized GetFragranceResponse synchronizedGetFragranceDetail(Long fragranceId, CustomUserDetails userDetails) {
        return fragranceService.getFragranceDetail(fragranceId, userDetails);
    }
}
