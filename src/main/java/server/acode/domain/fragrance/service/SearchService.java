package server.acode.domain.fragrance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.family.dto.response.PageableResponse;
import server.acode.domain.fragrance.dto.request.SearchCond;
import server.acode.domain.fragrance.dto.response.FragranceInfo;
import server.acode.domain.fragrance.dto.response.SearchBrandResponse;
import server.acode.global.common.PageRequest;

import static org.springframework.util.StringUtils.hasText;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {

    public SearchBrandResponse searchBrand(String search) {



        return null;
    }


    public PageableResponse<FragranceInfo> searchFragrance(SearchCond cond, PageRequest pageRequest) {
        Pageable pageable = pageRequest.of();
        String additionalFamily = null;

        // family에 값이 들어온 경우 값 처리
        if(hasText(cond.getFamily())) {
            String[] parts = cond.getFamily().split("\\s+", 2);
            cond.setFamily(parts.length > 0 ? parts[0] : null);
            additionalFamily = parts.length > 1 ? parts[1] : null;
        }



        return null;
    }
}
