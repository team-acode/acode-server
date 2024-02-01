package server.acode.domain.fragrance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.acode.domain.family.dto.SimilarFragranceOrCond;
import server.acode.domain.family.entity.Family;
import server.acode.domain.family.repository.FragranceFamilyRepository;
import server.acode.domain.fragrance.dto.response.*;
import server.acode.domain.fragrance.entity.Brand;
import server.acode.domain.fragrance.entity.Capacity;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.fragrance.repository.BrandRepository;
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

import static org.springframework.util.StringUtils.*;

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

    private final BrandRepository brandRepository;

    @Value("${store.image.siVillage}")
    String siVillage;

    @Value("${store.image.lotteOn}")
    String lotteOn;

    @Value("${store.image.kurly}")
    String kurly;

    @Value("${store.image.lg}")
    String lg;


    @Transactional
    public GetFragranceResponse getFragranceDetail(Long fragranceId, CustomUserDetails userDetails) {
        boolean isScraped = false;

        Fragrance fragrance = fragranceRepository.findById(fragranceId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRAGRANCE_NOT_FOUND));

        if (userDetails != null) {
            User user = userRepository.findById(Long.valueOf(userDetails.getUsername()))
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
        Map<String, Integer> reviewSeasonMap = reviewSeasonStatistics(reviewSeason, fragrance.getReviewCnt());

        ReviewLongevity reviewLongevity = reviewLongevityRepository.findByFragrance(fragrance)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_LONGEVITY_NOT_FOUND));
        Map<String, Integer> reviewLongevityMap = reviewLongevityStatistics(reviewLongevity, fragrance.getReviewCnt());

        ReviewIntensity reviewIntensity = reviewIntensityRepository.findByFragrance(fragrance)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_INTENSITY_NOT_FOUND));
        Map<String, Integer> reviewIntensityMap = reviewIntensityStatistics(reviewIntensity, fragrance.getReviewCnt());

        ReviewStyle reviewStyle = reviewStyleRepository.findByFragrance(fragrance)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_STYLE_NOT_FOUND));
        Map<String, Integer> reviewStyleMap = reviewStyleStatistics(reviewStyle, fragrance.getReviewCnt());

        return new GetFragranceReviewStatistics(
                reviewSeasonMap.entrySet().stream().map(ReviewStatInfo::new).toList(),
                reviewLongevityMap.entrySet().stream().map(ReviewStatInfo::new).toList(),
                reviewIntensityMap.entrySet().stream().map(ReviewStatInfo::new).toList(),
                reviewStyleMap.entrySet().stream().map(ReviewStatInfo::new).toList()
        );
    }

    private Map<String, Integer> reviewSeasonStatistics(ReviewSeason reviewSeason, int reviewCnt) {
        Map<String, Integer> fieldMap = new HashMap<>();

//        fieldMap.put("spring", reviewSeason.getSpring());
//        fieldMap.put("summer", reviewSeason.getSummer());
//        fieldMap.put("autumn", reviewSeason.getAutumn());
//        fieldMap.put("winter", reviewSeason.getWinter());
//        fieldMap.put("allSeasons", reviewSeason.getAll());

        Field[] fields = reviewSeason.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == int.class) {
                try {
                    fieldMap.put(field.getName(),
                            (int) Math.round(((double) field.getInt(reviewSeason) / reviewCnt) * 100));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } finally {
                    field.setAccessible(false);
                }
            }
        }

        return fieldMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing((e1, e2) -> {
                            String[] priorities = {"allSeasons", "winter", "autumn", "summer", "spring"};
                            return Integer.compare(
                                    getPriority(e1.getKey(), priorities),
                                    getPriority(e2.getKey(), priorities)
                            );
                        })
                )
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    // 선형 검색
    // 조금 더 최적화할 수 있는 방법이 있을지 고민인데 어차피 네다섯개 비교라 차이가 크지 않을 것 같다
    private int getPriority(String fieldName, String[] priorities) {
        for (int i = 0; i < priorities.length; i++) {
            if (fieldName.equals(priorities[i])) {
                return i;
            }
        }
        return priorities.length; // not found
    }

    private Map<String, Integer> reviewLongevityStatistics(ReviewLongevity reviewLongevity, int reviewCnt) {
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
                    fieldMap.put(field.getName(),
                            (int) Math.round(((double) field.getInt(reviewLongevity) / reviewCnt) * 100));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            field.setAccessible(false);
        }

        return fieldMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing((e1, e2) -> {
                            String[] priorities = {"fullday", "halfday", "fourhours", "onehour"};
                            return Integer.compare(
                                    getPriority(e1.getKey(), priorities),
                                    getPriority(e2.getKey(), priorities)
                            );
                        }))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private Map<String, Integer> reviewIntensityStatistics(ReviewIntensity reviewIntensity, int reviewCnt) {
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
                    fieldMap.put(field.getName(),
                            (int) Math.round(((double) field.getInt(reviewIntensity) / reviewCnt) * 100));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            field.setAccessible(false);
        }

        return fieldMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing((e1, e2) -> {
                            String[] priorities = {"intense", "strong", "medium", "weak"};
                            return Integer.compare(
                                    getPriority(e1.getKey(), priorities),
                                    getPriority(e2.getKey(), priorities)
                            );
                        }))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private Map<String, Integer> reviewStyleStatistics(ReviewStyle reviewStyle, int reviewCnt) {
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
                    fieldMap.put(field.getName(),
                            (int) Math.round(((double) field.getInt(reviewStyle) / reviewCnt) * 100));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            field.setAccessible(false);
        }

        return fieldMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
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

        boolean isSoldOut = false;
        if (!hasText(fragrance.getLink1())) {
            isSoldOut = true;
            return GetFragrancePurchase.builder()
                    .isSoldOut(isSoldOut)
                    .fragranceName(fragrance.getName())
                    .brandName(fragrance.getBrand().getKorName())
                    .build();
        }

        List<PurchaseInfo> purchaseList = new ArrayList<>();
        PurchaseInfo purchaseInfo1 = setPurchaseInfo(fragrance, fragrance.getLink1());
        purchaseList.add(purchaseInfo1);

        if (hasText(fragrance.getLink2())) {
            PurchaseInfo purchaseInfo2 = setPurchaseInfo(fragrance, fragrance.getLink2());
            purchaseList.add(purchaseInfo2);
        } else {
            return GetFragrancePurchase.builder()
                    .isSoldOut(isSoldOut)
                    .fragranceName(fragrance.getName())
                    .brandName(fragrance.getBrand().getKorName())
                    .purchaseList(purchaseList)
                    .build();
        }

        if (hasText(fragrance.getLink3())) {
            PurchaseInfo purchaseInfo3 = setPurchaseInfo(fragrance, fragrance.getLink3());
            purchaseList.add(purchaseInfo3);
        } else {
            return GetFragrancePurchase.builder()
                    .isSoldOut(isSoldOut)
                    .fragranceName(fragrance.getName())
                    .brandName(fragrance.getBrand().getKorName())
                    .purchaseList(purchaseList)
                    .build();
        }

        return GetFragrancePurchase.builder()
                .isSoldOut(isSoldOut)
                .fragranceName(fragrance.getName())
                .brandName(fragrance.getBrand().getKorName())
                .purchaseList(purchaseList)
                .build();
    }

    private PurchaseInfo setPurchaseInfo(Fragrance fragrance, String link) {
        PurchaseInfo purchaseInfo = PurchaseInfo.builder().link(link).build();

        if (purchaseInfo.getLink().startsWith("https://www.sivillage")) {
            purchaseInfo.setImage(siVillage);
            purchaseInfo.setTitle("S.I.VILLAGE 신세계인터내셔날 공식몰");
        } else if (purchaseInfo.getLink().contains(fragrance.getBrand().getEngName().replace(" ", "").toLowerCase())) { // 공홈
            purchaseInfo.setImage(fragrance.getBrand().getRoundImg());
//            StringBuilder sb = new StringBuilder(fragrance.getBrand().getEngName()).append(" ").append(fragrance.getBrand().getKorName());
            purchaseInfo.setTitle(fragrance.getBrand().getEngName() + " " + fragrance.getBrand().getKorName());
        } else if (fragrance.getBrand().getId() == 5L) { // 조말론
            Brand brand = brandRepository.findById(5L).orElseThrow(() -> new CustomException(ErrorCode.BRAND_NOT_FOUND));
            purchaseInfo.setImage(brand.getRoundImg());
            purchaseInfo.setTitle("JO MALONE LONDON 조말론 런던");
        } else if (purchaseInfo.getLink().startsWith("https://www.lotteon")) {
            purchaseInfo.setImage(lotteOn);
            purchaseInfo.setTitle("LOTTE ON 롯데온");
        } else if (purchaseInfo.getLink().startsWith("https://brand.naver.com/lg_perfumery")) {
            purchaseInfo.setImage(lg);
            purchaseInfo.setTitle("LG생활건강");
        } else if (purchaseInfo.getLink().startsWith("https://www.kurly")) {
            purchaseInfo.setImage(kurly);
            purchaseInfo.setTitle("KURLY 컬리");
        } else {
            throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
        }

        return purchaseInfo;
    }


    public GetFragranceReview getFragranceReview(Long fragranceId, PageRequest pageRequest) {
        Pageable pageable = pageRequest.of();

        Fragrance fragrance = fragranceRepository.findById(fragranceId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRAGRANCE_NOT_FOUND));

        Page<ReviewInfo> reviewInfoPage = reviewRepository.getReviewInfoPage(fragranceId, pageable);

        return GetFragranceReview.from(fragrance, reviewInfoPage);
    }


    @Transactional
    public ResponseEntity<?> scrap(Long fragranceId, Long userId) {
        User user = userRepository.findById(userId)
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
