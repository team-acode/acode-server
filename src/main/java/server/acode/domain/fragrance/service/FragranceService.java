package server.acode.domain.fragrance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.family.dto.SimilarFragranceOrCond;
import server.acode.domain.family.entity.Family;
import server.acode.domain.family.repository.FamilyRepository;
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
import server.acode.domain.review.entity.ReviewSeason;
import server.acode.domain.review.repository.*;
import server.acode.domain.user.entity.Scrap;
import server.acode.domain.user.entity.User;
import server.acode.domain.user.repository.ScrapRepository;
import server.acode.global.common.ErrorCode;
import server.acode.global.common.PageRequest;
import server.acode.global.exception.CustomException;

import java.util.*;
import java.util.stream.Collectors;

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

    private final ReviewRepository reviewRepository;

    private final ReviewSeasonRepository reviewSeasonRepository;
    private final ReviewLongevityRepository reviewLongevityRepository;
    private final ReviewIntensityRepository reviewIntensityRepository;
    private final ReviewStyleRepository reviewStyleRepository;

    private final FamilyRepository familyRepository;


    @Transactional
    public GetFragranceResponse getFragranceDetail(Long fragranceId, User user) {
        boolean isScraped = false;

        Fragrance fragrance = fragranceRepository.findById(fragranceId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRAGRANCE_NOT_FOUND));

        isScraped = scrapRepository.findByUserAndFragrance(user, fragrance).isPresent();

        List<Family> findFamilyList = fragranceFamilyRepository.findByFragrance(fragrance);
        List<FamilyInfo> familyList = findFamilyList.stream().map(FamilyInfo::new).toList();

        List<Capacity> findCapacityList = capacityRepository.findByFragrance(fragrance);
        List<CapacityInfo> capacityList = findCapacityList.stream().map(CapacityInfo::new).toList();

        //조회수 증가
        fragranceRepository.updateFragranceView(fragranceId);

        return new GetFragranceResponse(fragrance, isScraped, familyList, capacityList);
    }


    public GetFragranceNote getFragranceNote(Long fragranceId) {
        Fragrance fragrance = fragranceRepository.findById(fragranceId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRAGRANCE_NOT_FOUND));

        List<Ingredient> findTopNote = topNoteRepository.findByFragrance(fragrance);
        List<NoteInfo> topNote = findTopNote.stream().map(NoteInfo::new).toList();

        List<Ingredient> findMiddleNote = middleNoteRepository.findByFragrance(fragrance);
        List<NoteInfo> middleNote = findMiddleNote.stream().map(NoteInfo::new).toList();

        List<Ingredient> findBaseNote = baseNoteRepository.findByFragrance(fragrance);
        List<NoteInfo> baseNote = findBaseNote.stream().map(NoteInfo::new).toList();

        return new GetFragranceNote(fragrance, topNote, middleNote, baseNote);
    }


    public GetFragranceReviewPreview getFragranceReviewPreview(Long fragranceId) {
        Fragrance fragrance = fragranceRepository.findById(fragranceId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRAGRANCE_NOT_FOUND));

        List<ReviewPreview> reviewPreviewList = reviewRepository.getFragranceReviewPreview(fragrance);

        return new GetFragranceReviewPreview(fragrance, reviewPreviewList);
    }


    // TODO 향수 리뷰 통계 ㅠㅠ
//    public GetFragranceReviewStatistics getFragranceReviewStatistics(Long fragranceId) {
//        Map<String, Integer> fieldList = new HashMap<>();
//
//        ReviewSeason reviewSeason = reviewSeasonRepository.findByFragranceId(fragranceId)
//                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_SEASON_NOT_FOUND));
//
//        fieldList.put("spring", reviewSeason.getSpring());
//        fieldList.put("summer", reviewSeason.getSummer());
//        fieldList.put("autumn", reviewSeason.getAutumn());
//        fieldList.put("winter", reviewSeason.getWinter());
//
//        Integer maxValue = Collections.max(fieldList.values());
//        List<String> keyWithMaxValueList = fieldList.entrySet()
//                .stream()
//                .filter(entry -> entry.getValue().equals(maxValue))
//                .map(Map.Entry::getKey)
//                .toList();
//        if (keyWithMaxValueList.size() == 1) {
//            fieldList.get(keyWithMaxValueList.get(0));
//        }
//
//
//        return null;
//    }


    public GetFragranceSimilar getFragranceSimilar(Long fragranceId) {
        List<FragranceInfo> fragranceInfoList = new ArrayList<>();

        List<Long> familyIdList = fragranceFamilyRepository.searchFamilyByFragranceId(fragranceId);
        if (familyIdList.size() == 2) {
            fragranceInfoList = fragranceFamilyRepository.searchSimilarFragranceAnd(
                    fragranceId, familyIdList.get(0), familyIdList.get(1));
            if (fragranceInfoList.size() < 5) {
                List<FragranceInfo> additionalFragranceInfoList = fragranceFamilyRepository.searchSimilarFragranceOr(
                        getSimilarFragranceOrCond(fragranceId, familyIdList, fragranceInfoList));
                if (!additionalFragranceInfoList.isEmpty()) {
                    fragranceInfoList.addAll(additionalFragranceInfoList);
                }
            }
        } else {
            fragranceInfoList = fragranceFamilyRepository.searchSimilarFragrance(fragranceId, familyIdList.get(0));
        }

        return new GetFragranceSimilar(fragranceInfoList);
    }


    private SimilarFragranceOrCond getSimilarFragranceOrCond(Long fragranceId, List<Long> familyIdList, List<FragranceInfo> fragranceInfoList) {
        List<Long> selectedFragranceIdList = fragranceInfoList.stream().map(FragranceInfo::getFragranceId).collect(Collectors.toList());
        selectedFragranceIdList.add(0, fragranceId);
        return SimilarFragranceOrCond.from(familyIdList, selectedFragranceIdList);
    }


    public GetFragrancePurchase getFragrancePurchase(Long fragranceId) {
        Fragrance fragrance = fragranceRepository.findById(fragranceId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRAGRANCE_NOT_FOUND));
        return GetFragrancePurchase.from(fragrance);
    }


    public GetFragranceReview getFragranceReview(Long fragranceId, PageRequest pageRequest) {
        Pageable pageable = pageRequest.of();

        Fragrance fragrance = fragranceRepository.findById(fragranceId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRAGRANCE_NOT_FOUND));

        Page<ReviewInfo> reviewInfoPage = reviewRepository.getReviewInfoPage(fragranceId, pageable);

        return GetFragranceReview.from(fragrance, reviewInfoPage);
    }


    @Transactional
    public ResponseEntity<?> scrap(Long fragranceId, User user) {
        Fragrance fragrance = fragranceRepository.findById(fragranceId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRAGRANCE_NOT_FOUND));
        if (scrapRepository.findByUserAndFragrance(user, fragrance).isEmpty()) {
            scrapRepository.save(new Scrap(user, fragrance));
        } else {
            scrapRepository.deleteByUserAndFragrance(user, fragrance);
        }
        return ResponseEntity.ok().build();
    }

}
