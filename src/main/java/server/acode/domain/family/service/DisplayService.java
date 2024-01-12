package server.acode.domain.family.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import server.acode.domain.family.dto.request.FragranceFilterCond;
import server.acode.domain.family.dto.response.DisplayFragrance;
import server.acode.domain.family.dto.response.DisplayResponse;
import server.acode.domain.family.repository.FamilyRepository;
import server.acode.global.common.PageRequest;

@Service
@RequiredArgsConstructor
public class DisplayService {
    private final FamilyRepository familyRepository;

    public DisplayResponse searchFragranceList(FragranceFilterCond cond, PageRequest pageRequest){

        Pageable pageable = pageRequest.of();

        String s = cond.getFamily();
        String[] parts = (s != null) ? s.split("\\s+", 2) : null; // 문자열이 null이 아니라면 공백을 기준으로 나눔

        cond.setFamily(parts.length > 0 ? parts[0] : null);
        String additionalFamily = parts.length > 1 ? parts[1] : null;

        Page<DisplayFragrance> result = familyRepository.searchByCategory(cond, additionalFamily, pageable);

        // response dto로 변환
        DisplayResponse response = new DisplayResponse();
        response.setTotalPages(result.getTotalPages());
        response.setTotalElements(result.getTotalElements());
        response.setData(result.getContent());

        return response;
    }


}
