package server.acode.domain.fragrance.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import server.acode.domain.family.dto.response.DisplayFragrance;
import server.acode.domain.family.dto.response.QDisplayFragrance;
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
    public Page<DisplayFragrance> searchByIngredient(String ingredientName, Pageable pageable) {

        QueryResults<DisplayFragrance> results = queryFactory
                .select(new QDisplayFragrance(
                        fragrance.id.as("fragranceId"),
                        brand.korName.as("brandName"),
                        fragrance.name.as("fragranceName"),
                        fragrance.thumbnail
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

        List<DisplayFragrance> content = results.getResults();
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
    public List<Long> extractByScent(String scent1, String scent2, List<Long> fragranceIdList) {
        return queryFactory
                .select(fragrance.id)
                .from(fragrance)
                .where(
                        scentContainsIgnoreCase(scent1),
                        scentContainsIgnoreCase(scent2),
                        fragrance.id.in(fragranceIdList)
                )
                .fetch();
    }




    private BooleanExpression scentContainsIgnoreCase(String scent) {
        return hasText(scent)
                ? fragrance.scent.containsIgnoreCase(scent)
                : null;
    }


    @Override
    public List<Long> extractByScentOr(String scent1, String scent2, List<Long> fragranceIdList) {
        return queryFactory
                .select(fragrance.id)
                .from(fragrance)
                .where(
                        fragrance.id.in(fragranceIdList),
                        scentContainsIgnoreCase(scent1)
                                .or(scentContainsIgnoreCase(scent2))
                )
                .fetch();
    }


//    @Override
//    public List<Long> extractByScent(String scent, List<Long> fragranceIdList) {
//        return queryFactory
//                .select(fragrance.id)
//                .from(fragrance)
//                .where(
//                        fragrance.id.in(fragranceIdList),
//                        fragrance.scent.containsIgnoreCase(scent)
//                )
//                .fetch();
//    }


    @Override
    public List<Long> extractByStyle(String style1, String style2, List<Long> fragranceIdList) {
        return queryFactory
                .select(fragrance.id)
                .from(fragrance)
                .where(
                        fragrance.id.in(fragranceIdList),
                        styleContainsIgnoreCase(style1),
                        styleContainsIgnoreCase(style2)
                )
                .fetch();
    }


    private BooleanExpression styleContainsIgnoreCase(String style) {
        return hasText(style)
                ? fragrance.style.containsIgnoreCase(style)
                : null;
    }


    @Override
    public List<Long> extractByStyleOr(String style1, String style2, List<Long> fragranceIdList) {
        return queryFactory
                .select(fragrance.id)
                .from(fragrance)
                .where(
                        fragrance.id.in(fragranceIdList),
                        styleContainsIgnoreCase(style1)
                                .or(styleContainsIgnoreCase(style2))
                )
                .fetch();
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
