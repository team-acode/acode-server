package server.acode.domain.fragrance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.family.dto.SimilarFragranceOrCond;
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
import server.acode.domain.review.entity.ReviewIntensity;
import server.acode.domain.review.entity.ReviewLongevity;
import server.acode.domain.review.entity.ReviewSeason;
import server.acode.domain.review.entity.ReviewStyle;
import server.acode.domain.review.repository.*;
import server.acode.domain.user.entity.Scrap;
import server.acode.domain.user.entity.User;
import server.acode.domain.user.repository.ScrapRepository;
import server.acode.domain.user.repository.UserRepository;
import server.acode.global.auth.security.CustomUserDetails;
import server.acode.global.common.ErrorCode;
import server.acode.global.common.PageRequest;
import server.acode.global.exception.CustomException;

import java.lang.reflect.Field;
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

    private final UserRepository userRepository;


    @Transactional
    public GetFragranceResponse getFragranceDetail(Long fragranceId, CustomUserDetails userDetails) {
        boolean isScraped = false;

        Fragrance fragrance = fragranceRepository.findById(fragranceId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRAGRANCE_NOT_FOUND));

        if (userDetails != null) {
            User user = userRepository.findByAuthKey(userDetails.getUsername())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            isScraped = scrapRepository.findByUserAndFragrance(user, fragrance).isPresent();
        }

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


    public GetFragranceReviewStatistics getFragranceReviewStatistics(Long fragranceId) {
        Fragrance fragrance = fragranceRepository.findById(fragranceId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRAGRANCE_NOT_FOUND));

        ReviewSeason reviewSeason = reviewSeasonRepository.findByFragrance(fragrance)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_SEASON_NOT_FOUND));
        Map<String, Integer> reviewSeasonMap = reviewSeasonStatistics(reviewSeason);

        ReviewLongevity reviewLongevity = reviewLongevityRepository.findByFragrance(fragrance)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_LONGEVITY_NOT_FOUND));
        Map<String, Integer> reviewLongevityMap = reviewLongevityStatistics(reviewLongevity);

        ReviewIntensity reviewIntensity = reviewIntensityRepository.findByFragrance(fragrance)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_INTENSITY_NOT_FOUND));
        Map<String, Integer> reviewIntensityMap = reviewIntensityStatistics(reviewIntensity);

        ReviewStyle reviewStyle = reviewStyleRepository.findByFragrance(fragrance)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_STYLE_NOT_FOUND));
        Map<String, Integer> reviewStyleMap = reviewStyleStatistics(reviewStyle);

        return GetFragranceReviewStatistics.builder()
                .reviewCnt(fragrance.getReviewCnt())
                .season(reviewSeasonMap.entrySet().stream().findFirst().get().getKey())
                .seasonMax(reviewSeasonMap.entrySet().stream().findFirst().get().getValue())
                .longevity(reviewLongevityMap.entrySet().stream().findFirst().get().getKey())
                .longevityMax(reviewLongevityMap.entrySet().stream().findFirst().get().getValue())
                .intensity(reviewIntensityMap.entrySet().stream().findFirst().get().getKey())
                .intensityMax(reviewIntensityMap.entrySet().stream().findFirst().get().getValue())
                .style1(reviewStyleMap.entrySet().stream().findFirst().get().getKey())
                .styleMax1(reviewStyleMap.entrySet().stream().findFirst().get().getValue())
                .style2(reviewStyleMap.entrySet().stream().skip(1).map(Map.Entry::getKey).findFirst().get())
                .styleMax2(reviewStyleMap.entrySet().stream().skip(1).map(Map.Entry::getValue).findFirst().get())
                .style3(reviewStyleMap.entrySet().stream().skip(2).map(Map.Entry::getKey).findFirst().get())
                .styleMax3(reviewStyleMap.entrySet().stream().skip(2).map(Map.Entry::getValue).findFirst().get())
                .build();

//        String.valueOf(reviewSeasonMap.entrySet().stream().findFirst().map(Map.Entry::getValue))
//        Integer maxValue = Collections.max(fieldList.values());
//        List<String> keyWithMaxValueList = fieldList.entrySet()
//                .stream()
//                .filter(entry -> entry.getValue().equals(maxValue))
//                .map(Map.Entry::getKey)
//                .toList();
//        if (keyWithMaxValueList.size() == 1) {
//            fieldList.get(keyWithMaxValueList.get(0));
//        }
    }

    private Map<String, Integer> reviewSeasonStatistics(ReviewSeason reviewSeason) {
        Map<String, Integer> fieldMap = new HashMap<>();

//        fieldMap.put("spring", reviewSeason.getSpring());
//        fieldMap.put("summer", reviewSeason.getSummer());
//        fieldMap.put("autumn", reviewSeason.getAutumn());
//        fieldMap.put("winter", reviewSeason.getWinter());

        Field[] fields = reviewSeason.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == int.class) {
                try {
                    fieldMap.put(field.getName(), field.getInt(reviewSeason));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            field.setAccessible(false);
        }

        return fieldMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private Map<String, Integer> reviewLongevityStatistics(ReviewLongevity reviewLongevity) {
        Map<String, Integer> fieldMap = new HashMap<>();
//        fieldMap.put("onehour", reviewLongevity.getOnehour());
//        fieldMap.put("fourhours", reviewLongevity.getFourhours());
//        fieldMap.put("halfday", reviewLongevity.getHalfday());
//        fieldMap.put("fullday", reviewLongevity.getFullday());

        Field[] fields = reviewLongevity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == int.class) {
                try {
                    fieldMap.put(field.getName(), field.getInt(reviewLongevity));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            field.setAccessible(false);
        }

        return fieldMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private Map<String, Integer> reviewIntensityStatistics(ReviewIntensity reviewIntensity) {
        Map<String, Integer> fieldMap = new HashMap<>();
//        fieldMap.put("weak", reviewIntensity.getWeak());
//        fieldMap.put("medium", reviewIntensity.getMedium());
//        fieldMap.put("strong", reviewIntensity.getStrong());
//        fieldMap.put("intense", reviewIntensity.getIntense());

        Field[] fields = reviewIntensity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == int.class) {
                try {
                    fieldMap.put(field.getName(), field.getInt(reviewIntensity));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            field.setAccessible(false);
        }

        return fieldMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private Map<String, Integer> reviewStyleStatistics(ReviewStyle reviewStyle) {
        Map<String, Integer> fieldMap = new HashMap<>();
//        fieldMap.put("chic", reviewStyle.getChic());
//        fieldMap.put("mature", reviewStyle.getMature());
//        fieldMap.put("luxurious", reviewStyle.getLuxurious());
//        fieldMap.put("elegant", reviewStyle.getElegant());
//        fieldMap.put("masculine", reviewStyle.getMasculine());
//        fieldMap.put("comfortable", reviewStyle.getComfortable());
//        fieldMap.put("serene", reviewStyle.getSerene());
//        fieldMap.put("light", reviewStyle.getLight());
//        fieldMap.put("neutral", reviewStyle.getNeutral());
//        fieldMap.put("friendly", reviewStyle.getFriendly());
//        fieldMap.put("clean", reviewStyle.getClean());
//        fieldMap.put("sensual", reviewStyle.getSensual());
//        fieldMap.put("delicate", reviewStyle.getDelicate());
//        fieldMap.put("lively", reviewStyle.getLively());
//        fieldMap.put("lovely", reviewStyle.getLovely());
//        fieldMap.put("bright", reviewStyle.getBright());
//        fieldMap.put("radiant", reviewStyle.getRadiant());
//        fieldMap.put("feminine", reviewStyle.getFeminine());
//        fieldMap.put("innocent", reviewStyle.getInnocent());
//        fieldMap.put("weighty", reviewStyle.getWeighty());
//        fieldMap.put("soft", reviewStyle.getSoft());
//        fieldMap.put("cozy", reviewStyle.getCozy());

        Field[] fields = reviewStyle.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == int.class) {
                try {
                    fieldMap.put(field.getName(), field.getInt(reviewStyle));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            field.setAccessible(false);
        }

        return fieldMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private Map<String, Integer> sortByValue(Map<String, Integer> unsortedMap) {
        // Map.Entry 리스트 작성
        List<Map.Entry<String, Integer>> entryList = new LinkedList<>(unsortedMap.entrySet());

        // 값으로 정렬
        entryList.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));

        // 정렬된 값을 가진 LinkedHashMap 반환
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }


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
    public ResponseEntity<?> scrap(Long fragranceId, CustomUserDetails userDetails) {
        User user = userRepository.findByAuthKey(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
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
