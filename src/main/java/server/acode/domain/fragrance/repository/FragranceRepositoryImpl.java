package server.acode.domain.fragrance.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import server.acode.domain.family.dto.response.FragranceCatalogDto;
import server.acode.domain.family.dto.response.QFragranceCatalogDto;
import server.acode.domain.fragrance.entity.Concentration;

import java.util.List;

import static org.springframework.util.StringUtils.*;
import static server.acode.domain.fragrance.entity.QBrand.brand;
import static server.acode.domain.fragrance.entity.QFragrance.*;
import static server.acode.domain.ingredient.entity.QBaseNote.baseNote;
import static server.acode.domain.ingredient.entity.QIngredient.ingredient;
import static server.acode.domain.ingredient.entity.QMiddleNote.middleNote;
import static server.acode.domain.ingredient.entity.QTopNote.topNote;

@Repository
public class FragranceRepositoryImpl implements FragranceRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public FragranceRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void findWithPessimisticLockById(Long fragranceId) {
        queryFactory.select(
                fragrance.rateSum,
                fragrance.reviewCnt
                )
                .from(fragrance)
                .where(fragrance.id.eq(fragranceId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
    }

    @Override
    public Page<FragranceCatalogDto> searchByIngredient(String ingredientName, Pageable pageable) {

        QueryResults<FragranceCatalogDto> results = queryFactory
                .select(new QFragranceCatalogDto(
                        fragrance.id.as("fragranceId"),
                        brand.korName.as("brandName"),
                        fragrance.name.as("fragranceName"),
                        fragrance.thumbnail,
                        fragrance.concentration
                ))
                .from(fragrance)
                .join(fragrance.brand, brand)
                .join(topNote).on(fragrance.id.eq(topNote.fragrance.id))
                .join(middleNote).on(fragrance.id.eq(middleNote.fragrance.id))
                .join(baseNote).on(fragrance.id.eq(baseNote.fragrance.id))
                .join(ingredient).on(topNote.ingredient.id.eq(ingredient.id)
                        .or(middleNote.ingredient.id.eq(ingredient.id))
                        .or(baseNote.ingredient.id.eq(ingredient.id))
                )
                .where(ingredient.korName.eq(ingredientName))
                .groupBy(fragrance.id)
                .fetchResults();

        List<FragranceCatalogDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


    @Override
    public List<Long> extractByConcentrationAndSeason(String concentrationCond, String seasonCond) {
        return queryFactory
                .select(fragrance.id)
                .from(fragrance)
                .where(
                        fragrance.concentration.eq(Concentration.valueOf(concentrationCond)),
                        fragrance.season.containsIgnoreCase(seasonCond)
                )
                .fetch();
    }



    @Override
    public List<Long> extractByConcentration(String concentration1, String concentration2) {
        return queryFactory
                .select(fragrance.id)
                .from(fragrance)
                .where(
                        concentrationEq(concentration1),
                        concentrationEq(concentration2)
                )
                .fetch();
    }

    @Override
    public List<Long> extractByConcentrationOr(String concentration1, String concentration2) {
        return queryFactory
                .select(fragrance.id)
                .from(fragrance)
                .where(
                        concentrationEq(concentration1)
                                .or(concentrationEq(concentration2))
                )
                .fetch();
    }

    private BooleanExpression concentrationEq(String concentration) {
        return hasText(concentration)
                ? fragrance.concentration.eq(Concentration.valueOf(concentration))
                : null;
    }



    @Override
    public List<Long> extractBySeason(String season1, String season2, List<Long> fragranceIdList) {
        return queryFactory
                .select(fragrance.id)
                .from(fragrance)
                .where(
                        fragrance.id.in(fragranceIdList),
                        seasonContains(season1),
                        seasonContains(season2)
                )
                .fetch();
    }

    @Override
    public List<Long> extractBySeasonOr(String season1, String season2, List<Long> fragranceIdList) {
        return queryFactory.select(fragrance.id)
                .from(fragrance)
                .where(
                        fragrance.id.in(fragranceIdList),
                        seasonContains(season1)
                                .or(seasonContains(season2))
                )
                .fetch();
    }

    @Override
    public List<Long> extractByOnlySeasonOr(String season1, String season2) {
        return queryFactory.select(fragrance.id)
                .from(fragrance)
                .where(
                        seasonContains(season1)
                        .or(seasonContains(season2))
                )
                .fetch();
    }

    private BooleanExpression seasonContains(String season) {
        return hasText(season)
                ? fragrance.season.contains(season)
                : null;
    }



    @Override
    public List<Long> extractByScent(String scent1, String scent2, List<Long> fragranceIdList) {
        return queryFactory
                .select(fragrance.id)
                .from(fragrance)
                .where(
                        fragrance.id.in(fragranceIdList),
                        scentContains(scent1),
                        scentContains(scent2)
                )
                .fetch();
    }

    @Override
    public List<Long> extractByOnlyScent(String scent1, String scent2) {
        return queryFactory
                .select(fragrance.id)
                .from(fragrance)
                .where(
                        scentContains(scent1),
                        scentContains(scent2)
                )
                .fetch();
    }

    @Override
    public List<Long> extractByScentOr(String scent1, String scent2, List<Long> fragranceIdList) {
        return queryFactory
                .select(fragrance.id)
                .from(fragrance)
                .where(
                        fragrance.id.in(fragranceIdList),
                        scentContains(scent1)
                                .or(scentContains(scent2))
                )
                .fetch();
    }

    @Override
    public List<Long> extractByOnlyScentOr(String scent1, String scent2) {
        return queryFactory
                .select(fragrance.id)
                .from(fragrance)
                .where(
                        scentContains(scent1)
                        .or(scentContains(scent2))
                )
                .fetch();
    }

    private BooleanExpression scentContains(String scent) {
        return hasText(scent)
                ? fragrance.scent.contains(scent)
                : null;
    }



    @Override
    public List<Long> extractByStyle(String style1, String style2, List<Long> fragranceIdList) {
        return queryFactory
                .select(fragrance.id)
                .from(fragrance)
                .where(
                        fragrance.id.in(fragranceIdList),
                        styleContains(style1),
                        styleContains(style2)
                )
                .fetch();
    }

    @Override
    public List<Long> extractByOnlyStyle(String style1, String style2) {
        return queryFactory
                .select(fragrance.id)
                .from(fragrance)
                .where(
                        styleContains(style1),
                        styleContains(style2)
                )
                .fetch();
    }

    @Override
    public List<Long> extractByStyleOr(String style1, String style2, List<Long> fragranceIdList) {
        return queryFactory
                .select(fragrance.id)
                .from(fragrance)
                .where(
                        fragrance.id.in(fragranceIdList),
                        styleContains(style1)
                                .or(styleContains(style2))
                )
                .fetch();
    }

    @Override
    public List<Long> extractByOnlyStyleOr(String style1, String style2) {
        return queryFactory
                .select(fragrance.id)
                .from(fragrance)
                .where(
                        styleContains(style1)
                        .or(styleContains(style2))
                )
                .fetch();
    }


    private BooleanExpression styleContains(String style) {
        return hasText(style)
                ? fragrance.style.contains(style)
                : null;
    }


//    @Override
//    public List<Long> extractByStyle(String style, List<Long> fragranceIdList) {
//        return queryFactory
//                .select(fragrance.id)
//                .from(fragrance)
//                .where(
//                        fragrance.id.in(fragranceIdList),
//                        fragrance.style.containsIgnoreCase(style)
//                )
//                .fetch();
//    }


}
