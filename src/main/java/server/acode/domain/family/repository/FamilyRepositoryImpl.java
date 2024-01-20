package server.acode.domain.family.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import server.acode.domain.family.dto.request.FragranceFilterCond;
import server.acode.domain.family.dto.response.DisplayFragrance;
import server.acode.domain.family.dto.response.HomeFragrance;
import server.acode.domain.family.dto.response.QDisplayFragrance;
import server.acode.domain.family.dto.response.QHomeFragrance;
import server.acode.domain.family.entity.QFamily;
import server.acode.domain.family.entity.QFragranceFamily;
import server.acode.domain.fragrance.dto.response.ExtractFamily;
import server.acode.domain.fragrance.dto.response.QExtractFamily;
import server.acode.domain.fragrance.entity.QFragrance;



import java.util.List;

import static org.springframework.util.StringUtils.*;
import static server.acode.domain.family.entity.QFamily.*;
import static server.acode.domain.family.entity.QFragranceFamily.*;
import static server.acode.domain.fragrance.entity.QBrand.brand;
import static server.acode.domain.fragrance.entity.QFragrance.*;
import static server.acode.domain.ingredient.entity.QBaseNote.*;
import static server.acode.domain.ingredient.entity.QIngredient.*;
import static server.acode.domain.ingredient.entity.QMiddleNote.*;
import static server.acode.domain.ingredient.entity.QTopNote.*;


@Repository
public class FamilyRepositoryImpl implements FamilyRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public FamilyRepositoryImpl(EntityManager em) {
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

        //TODO 필요에 따라 조인 할 수 있는지 체크
        QueryResults<DisplayFragrance> results = queryFactory
                .select(new QDisplayFragrance(
                        fragrance.id.as("fragranceId"),
                        brand.korName.as("brandName"),
                        fragrance.name.as("fragranceName"),
                        fragrance.thumbnail
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
    public List<ExtractFamily> extractFamilies(List<Long> fragranceIdList){
                return queryFactory.select(new QExtractFamily(
                family.korName,
                family.engName,
                family.summary,
                family.icon,
                family.keyword
        ))
                .from(family)
                .where(family.id.in(fragranceIdList))
                .fetch();
    }

    private BooleanExpression brandNameEq(String brandName) {
        return hasText(brandName)? brand.korName.eq(brandName): null;
    }

    private BooleanExpression familyNameEq(String familyName) {
        return hasText(familyName)? family.korName.eq(familyName): null;
    }

    private BooleanExpression additionalFamilyNameEq(String familyName){
        // 서브 쿼리를 위한 QEntity 추가 생성
        QFragranceFamily fragranceFamilySub = new QFragranceFamily("fragranceFamilySub");
        QFamily familySub = new QFamily("familySub");
        QFragrance fragranceSub = new QFragrance("fragranceSub");

        return hasText(familyName)? fragrance.id.in(
                JPAExpressions.select(fragranceSub.id)
                        .from(fragranceFamilySub)
                        .join(fragranceFamilySub.family, familySub)
                        .join(fragranceFamilySub.fragrance, fragranceSub)
                        .where(familySub.korName.eq(familyName)))
                : null;
    }


}
