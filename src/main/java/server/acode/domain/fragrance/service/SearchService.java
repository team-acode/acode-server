package server.acode.domain.fragrance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.family.dto.response.PageableResponse;
import server.acode.domain.family.repository.FragranceFamilyRepository;
import server.acode.domain.fragrance.dto.request.SearchCond;
import server.acode.domain.fragrance.dto.response.BrandInfo;
import server.acode.domain.fragrance.dto.response.FragranceInfo;
import server.acode.domain.fragrance.dto.response.SearchBrandResponse;
import server.acode.domain.fragrance.repository.BrandRepository;
import server.acode.global.common.ErrorCode;
import server.acode.global.common.PageRequest;
import server.acode.global.exception.CustomException;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {
    private final BrandRepository brandRepository;
    private final FragranceFamilyRepository fragranceFamilyRepository;

    public PageableResponse<BrandInfo> searchBrand(String search, PageRequest pageRequest) {
        if (search.isBlank()) { // null 또는 "" 또는 공백문자열
            throw new CustomException(ErrorCode.SEARCH_NOT_FOUND);
        }

        Pageable pageable = pageRequest.of();

        Page<BrandInfo> brandList = brandRepository.searchBrand(search, pageable);

        return new PageableResponse<>(brandList);
    }


    public PageableResponse<FragranceInfo> searchFragrance(SearchCond cond, PageRequest pageRequest) {
        if (cond.getSearch().isBlank()) { // null 또는 "" 또는 공백문자열
            throw new CustomException(ErrorCode.SEARCH_NOT_FOUND);
        }

        Pageable pageable = pageRequest.of();
        String additionalFamily = null;

        // family에 값이 들어온 경우 값 처리
        if(hasText(cond.getFamily())) {
            String[] parts = cond.getFamily().split("\\s+", 2);
            cond.setFamily(parts.length > 0 ? parts[0] : null);
            additionalFamily = parts.length > 1 ? parts[1] : null;
        }

        Page<FragranceInfo> fragranceList = fragranceFamilyRepository.searchFragrance(cond, additionalFamily, pageable);

        return new PageableResponse<>(fragranceList);
    }
}
