package server.acode.domain.family.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import server.acode.domain.family.dto.request.FragranceCategoryCond;
import server.acode.domain.family.dto.response.DisplayFragrance;
import server.acode.domain.family.repository.FamilyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisplayService {
    private final FamilyRepository familyRepository;

    public Page<DisplayFragrance> searchFragranceList(FragranceCategoryCond cond,Pageable pageable){
        System.out.println("service");
        return familyRepository.searchByCategory(cond, pageable);

    }


}
