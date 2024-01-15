package server.acode.domain.family.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import server.acode.domain.family.dto.SimilarFragranceOrCond;
import server.acode.domain.family.entity.QFragranceFamily;
import server.acode.domain.fragrance.dto.response.FragranceInfo;
import server.acode.domain.fragrance.dto.response.QFragranceInfo;
import server.acode.domain.fragrance.entity.QFragrance;

import java.util.List;

import static server.acode.domain.family.entity.QFragranceFamily.*;
import static server.acode.domain.fragrance.entity.QFragrance.fragrance;

@Repository
public class FragranceFamilyRepositoryImpl implements FragranceFamilyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public FragranceFamilyRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
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
                        fragrance.brand.korName
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
                        fragrance.brand.korName
                ))
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
                        fragrance.brand.korName
                ))
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
}
