package server.acode.domain.fragrance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.family.entity.Family;
import server.acode.domain.family.repository.FragranceFamilyRepository;
import server.acode.domain.fragrance.dto.response.*;
import server.acode.domain.fragrance.entity.Capacity;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.fragrance.repository.CapacityRepository;
import server.acode.domain.fragrance.repository.FragranceRepository;
import server.acode.domain.ingredient.entity.Ingredient;
import server.acode.domain.ingredient.repository.BaseNoteRepository;
import server.acode.domain.ingredient.repository.MiddleNoteRepository;
import server.acode.domain.ingredient.repository.TopNoteRepository;
import server.acode.domain.user.entity.User;
import server.acode.domain.user.repository.ScrapRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FragranceService {
    private final FragranceRepository fragranceRepository;

    private final ScrapRepository scrapRepository;
    private final FragranceFamilyRepository fragranceFamilyRepository;
    private final CapacityRepository capacityRepository;

    private final TopNoteRepository topNoteRepository;
    private final MiddleNoteRepository middleNoteRepository;
    private final BaseNoteRepository baseNoteRepository;

    public GetFragranceResponse getFragranceDetail(Long fragranceId, User user) {
        boolean isScraped = false;

        Fragrance fragrance = fragranceRepository.findById(fragranceId)
                .orElseThrow(RuntimeException::new); //FRAGRANCE_NOT_FOUND

        isScraped = scrapRepository.findByUserAndFragrance(user, fragrance).isPresent();

        List<Family> findFamilyList = fragranceFamilyRepository.findByFragrance(fragrance);
        List<FamilyInfo> familyList = findFamilyList.stream().map(FamilyInfo::new).toList();

        List<Capacity> findCapacityList = capacityRepository.findByFragrance(fragrance);
        List<CapacityInfo> capacityList = findCapacityList.stream().map(CapacityInfo::new).toList();

        return new GetFragranceResponse(fragrance, isScraped, familyList, capacityList);
    }


    public GetFragranceNote getFragranceNote(Long fragranceId) {
        Fragrance fragrance = fragranceRepository.findById(fragranceId)
                .orElseThrow(RuntimeException::new); //FRAGRANCE_NOT_FOUND

        List<Ingredient> findTopNote = topNoteRepository.findByFragrance(fragrance);
        List<NoteInfo> topNote = findTopNote.stream().map(NoteInfo::new).toList();

        List<Ingredient> findMiddleNote = middleNoteRepository.findByFragrance(fragrance);
        List<NoteInfo> middleNote = findMiddleNote.stream().map(NoteInfo::new).toList();

        List<Ingredient> findBaseNote = baseNoteRepository.findByFragrance(fragrance);
        List<NoteInfo> baseNote = findBaseNote.stream().map(NoteInfo::new).toList();

        return new GetFragranceNote(fragrance, topNote, middleNote, baseNote);
    }
}
