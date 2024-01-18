package server.acode.domain.fragrance.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.dto.response.ExtractFamily;
import server.acode.domain.fragrance.dto.response.QExtractFamily;
import server.acode.domain.fragrance.entity.Concentration;

import java.util.List;

import static org.springframework.util.StringUtils.*;
import static server.acode.domain.fragrance.entity.QFragrance.*;

@Repository
public class FragranceRepositoryImpl implements FragranceRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public FragranceRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
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
