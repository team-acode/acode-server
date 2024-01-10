package server.acode.domain.family.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.acode.domain.family.dto.request.FragranceSearchCond;
import server.acode.domain.family.dto.response.FragranceByCatgegory;
import server.acode.domain.family.repository.FamilyRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class HomeController {

    private final FamilyRepository familyRepository;

    @GetMapping("/home")
    public List<FragranceByCatgegory> searchMemberV1(FragranceSearchCond condition){
        return familyRepository.search(condition);
    }

}
