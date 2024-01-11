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
import server.acode.domain.family.dto.request.FragranceCategoryCond;
import server.acode.domain.family.dto.request.FragranceSearchCond;
import server.acode.domain.family.dto.response.DisplayFragrance;
import server.acode.domain.family.dto.response.HomeFragrance;
import server.acode.domain.family.dto.response.QDisplayFragrance;
import server.acode.domain.family.dto.response.QHomeFragrance;
import server.acode.domain.family.entity.QFamily;
import server.acode.domain.family.entity.QFragranceFamily;
import server.acode.domain.fragrance.entity.QFragrance;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.util.StringUtils.*;
import static server.acode.domain.family.entity.QFamily.*;
import static server.acode.domain.family.entity.QFragranceFamily.*;
import static server.acode.domain.fragrance.entity.QBrand.brand;
import static server.acode.domain.fragrance.entity.QFragrance.*;

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
    public Page<DisplayFragrance> searchByCategory(FragranceCategoryCond cond, Pageable pageable) {
        QFragranceFamily fragranceFamilySub = new QFragranceFamily("fragranceFamilySub");
        QFamily familySub = new QFamily("familySub");
        QFragrance fragranceSub = new QFragrance("fragranceSub");

        System.out.println("repository");
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
                        fragrance.id.in(
                                JPAExpressions.select(fragranceSub.id)
                                        .from(fragranceFamilySub)
                                        .join(fragranceFamilySub.family, familySub)
                                        .join(fragranceFamilySub.fragrance, fragranceSub)
                                        .where(familySub.korName.eq("우디"))
                        )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();



        List<DisplayFragrance> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression brandNameEq(String brandName) {
        return hasText(brandName)? brand.korName.eq(brandName): null;
    }

    private BooleanExpression familyNameEq(String familyName) {
        return hasText(familyName)? family.korName.eq(familyName): null;
    }


}
