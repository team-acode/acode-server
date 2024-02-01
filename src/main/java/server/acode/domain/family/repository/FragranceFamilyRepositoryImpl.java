package server.acode.domain.family.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import server.acode.domain.family.dto.SimilarFragranceOrCond;
import server.acode.domain.family.dto.request.FragranceFilterCond;
import server.acode.domain.family.dto.response.DisplayFragrance;
import server.acode.domain.family.dto.response.HomeFragrance;
import server.acode.domain.family.dto.response.QDisplayFragrance;
import server.acode.domain.family.dto.response.QHomeFragrance;
import server.acode.domain.family.entity.QFragranceFamily;
import server.acode.domain.fragrance.dto.request.SearchCond;
import server.acode.domain.fragrance.dto.response.*;
import server.acode.domain.fragrance.entity.QFragrance;

import java.util.List;

import static org.springframework.util.StringUtils.*;
import static server.acode.domain.family.entity.QFamily.*;
import static server.acode.domain.family.entity.QFragranceFamily.*;
import static server.acode.domain.fragrance.entity.QBrand.brand;
import static server.acode.domain.fragrance.entity.QFragrance.fragrance;

@Repository
public class FragranceFamilyRepositoryImpl implements FragranceFamilyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public FragranceFamilyRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<HomeFragrance> search(String familyName) {
        return queryFactory
                .select(new QHomeFragrance(
                        fragrance.id.as("fragranceId"),
                        fragrance.name.as("fragranceName"),
                        brand.korName.as("korBrand"),
                        fragrance.style,
                        fragrance.poster
                ))
                .from(fragranceFamily)
                .join(fragranceFamily.family, family)
                .join(fragranceFamily.fragrance, fragrance)
                .join(fragrance.brand, brand)
                .where(family.korName.eq(familyName),
                        fragrance.poster.isNotNull().and(fragrance.poster.ne(""))
                )
                .orderBy(fragrance.view.desc())
                .limit(6)
                .fetch();

    }

    @Override
    public Page<DisplayFragrance> searchByFilter(FragranceFilterCond cond, String additionalFamily, Pageable pageable) {

        //TODO 카운트 쿼리 분리
        QueryResults<DisplayFragrance> results = queryFactory
                .select(new QDisplayFragrance(
                        fragrance.id.as("fragranceId"),
                        brand.korName.as("brandName"),
                        fragrance.name.as("fragranceName"),
                        fragrance.thumbnail,
                        fragrance.concentration
                ))
                .from(fragranceFamily)
                .join(fragranceFamily.fragrance, fragrance)
                .join(fragranceFamily.family, family)
                .join(fragrance.brand, brand)
                .where(brandNameEq(cond.getBrand()),
                        familyNameEq(cond.getFamily()),
                        additionalFamilyNameEq(additionalFamily)
                )
                .groupBy(fragrance.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();


        List<DisplayFragrance> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


    @Override
    public List<Long> searchFamilyByFragranceId(Long fragranceId) {
        return queryFactory
                .select(fragranceFamily.family.id)
                .from(fragranceFamily)
                .where(fragranceFamily.fragrance.id.eq(fragranceId))
                .fetch();
    }


    @Override
    public List<FragranceInfo> searchSimilarFragranceAnd(Long fragranceId, Long familyId1, Long familyId2) {
        return queryFactory
                .select(new QFragranceInfo(
                        fragrance.id.as("fragranceId"),
                        fragrance.thumbnail,
                        fragrance.name.as("fragranceName"),
                        fragrance.brand.korName.as("brandName"),
                        fragrance.concentration
                ))
                .from(fragranceFamily)
                .join(fragranceFamily.fragrance, fragrance)
                .where(
                        fragrance.id.ne(fragranceId),
                        fragranceFamily.family.id.eq(familyId1),
                        additionalFamilyIdEq(familyId2)
                )
                .orderBy(fragrance.view.desc())
                .limit(5)
                .fetch();
    }


    private BooleanExpression additionalFamilyIdEq(Long familyId) {
        // 서브 쿼리를 위한 QEntity 추가 생성
        QFragranceFamily fragranceFamilySub = new QFragranceFamily("fragranceFamilySub");
        QFragrance fragranceSub = new QFragrance("fragranceSub");

        return fragrance.id.in(
                JPAExpressions.select(fragranceSub.id)
                        .from(fragranceFamilySub)
                        .join(fragranceFamilySub.fragrance, fragranceSub)
                        .where(fragranceFamilySub.family.id.eq(familyId))
        );
    }


    @Override
    public List<FragranceInfo> searchSimilarFragranceOr(SimilarFragranceOrCond cond) {
        return queryFactory
                .select(new QFragranceInfo(
                        fragrance.id.as("fragranceId"),
                        fragrance.thumbnail,
                        fragrance.name.as("fragranceName"),
                        fragrance.brand.korName.as("brandName"),
                        fragrance.concentration))
                .from(fragranceFamily)
                .join(fragranceFamily.fragrance, fragrance)
                .where(
                        fragrance.id.notIn(cond.getSelectedFragranceIdList())
                                .and(fragranceFamily.family.id.eq(cond.getFamilyId1())
                                        .or(fragranceFamily.family.id.eq(cond.getFamilyId2())))
                )
                .limit(cond.getCount())
                .orderBy(fragrance.view.desc())
                .fetch();
    }


    @Override
    public List<FragranceInfo> searchSimilarFragrance(Long fragranceId, Long familyId) {
        return queryFactory
                .select(new QFragranceInfo(
                        fragrance.id.as("fragranceId"),
                        fragrance.thumbnail,
                        fragrance.name.as("fragranceName"),
                        fragrance.brand.korName.as("brandName"),
                        fragrance.concentration))
                .from(fragranceFamily)
                .join(fragranceFamily.fragrance, fragrance)
                .where(
                        fragrance.id.ne(fragranceId),
                        fragranceFamily.family.id.eq(familyId)
                )
                .limit(5)
                .orderBy(fragrance.view.desc())
                .fetch();
    }


    @Override
    public List<Long> extractByMainFamily(String mainFamily1, String mainFamily2, List<Long> fragranceIdList) {
        return queryFactory
                .select(fragranceFamily.fragrance.id) //.distinct()
                .from(fragranceFamily)
                .where(
                        fragrance.id.in(fragranceIdList),
                        mainFamilyNameEq(mainFamily1),
                        mainFamilyNameEq(mainFamily2)
                )
                .fetch();
    }

    @Override
    public List<Long> extractByMainFamilyOr(String mainFamily1, String mainFamily2, List<Long> fragranceIdList) {
        return queryFactory
                .select(fragranceFamily.fragrance.id)
                .from(fragranceFamily)
                .where(
                        fragrance.id.in(fragranceIdList),
                        mainFamilyNameEq(mainFamily1)
                                .or(mainFamilyNameEq(mainFamily2))
                )
                .fetch();
    }

    private BooleanExpression mainFamilyNameEq(String mainFamily) {
        return hasText(mainFamily)
                ? fragranceFamily.family.mainFamily.name.eq(mainFamily)
                : null;
    }

    @Override
    public List<FamilyCountDto> countFamily(List<Long> fragranceIdList) {
        return queryFactory
                .select(new QFamilyCountDto(
                        fragranceFamily.family.id,
                        fragranceFamily.family.count().intValue()
                ))
                .from(fragranceFamily)
                .where(fragranceFamily.fragrance.id.in(fragranceIdList))
                .groupBy(fragranceFamily.family)
                .orderBy(fragranceFamily.family.count().desc())
                .fetch();
    }

    @Override
    public List<ExtractFragrance> extractFragrance(List<Long> familyIdList) {
        return queryFactory
                .selectDistinct(new QExtractFragrance(
                                fragrance.id,
                                fragrance.name,
                                fragrance.brand.korName,
                                fragranceFamily.family.korName,
                                fragrance.thumbnail
                        )
                )
                .from(fragranceFamily)
                .where(fragranceFamily.family.id.in(familyIdList))
                .fetch();
    }


    @Override
    public Page<FragranceInfo> searchFragrance(SearchCond cond, String additionalFamily, Pageable pageable) {
        List<FragranceInfo> contents = queryFactory
                .select(new QFragranceInfo(
                        fragrance.id.as("fragranceId"),
                        fragrance.thumbnail,
                        fragrance.name.as("fragranceName"),
                        fragrance.brand.korName.as("brandName"),
                        fragrance.concentration)).distinct()
                .from(fragranceFamily)
                .where(
                        fragranceNameContains(cond.getSearch())
                                .or(korBrandNameContains(cond.getSearch()))
                                .or(engBrandNameContains(cond.getSearch())),
                        familyNameEq(cond.getFamily()),
                        additionalFamilyNameEq(additionalFamily)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(fragrance.count())
                .from(fragranceFamily)
                .where(
                        fragranceNameContains(cond.getSearch())
                                .or(korBrandNameContains(cond.getSearch()))
                                .or(engBrandNameContains(cond.getSearch())),
                        familyNameEq(cond.getFamily()),
                        additionalFamilyNameEq(additionalFamily)
                );

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    private BooleanExpression fragranceNameContains(String search) {
        return hasText(search)
                ? fragrance.name.containsIgnoreCase(search)
                : null;
    }

    private BooleanExpression korBrandNameContains(String search) {
        return hasText(search)
                ? fragrance.brand.korName.containsIgnoreCase(search)
                : null;
    }

    private BooleanExpression engBrandNameContains(String search) {
        return hasText(search)
                ? fragrance.brand.engName.containsIgnoreCase(search)
                : null;
    }

    private BooleanExpression brandNameEq(String brandName) {
        return hasText(brandName)? brand.korName.eq(brandName): null;
    }

    private BooleanExpression familyNameEq(String familyName) {
        return hasText(familyName) ? family.korName.eq(familyName) : null;
    }

    private BooleanExpression additionalFamilyNameEq(String additionalFamily) {
        // 서브 쿼리를 위한 QEntity 추가 생성
        QFragranceFamily fragranceFamilySub = new QFragranceFamily("fragranceFamilySub");

        return hasText(additionalFamily)
                ? fragrance.id.in(
                JPAExpressions.select(fragranceFamilySub.fragrance.id)
                        .from(fragranceFamilySub)
                        .where(fragranceFamilySub.family.korName.eq(additionalFamily)))
                : null;
    }

}
