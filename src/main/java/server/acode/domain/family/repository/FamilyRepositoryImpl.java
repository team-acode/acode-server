package server.acode.domain.family.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import server.acode.domain.family.dto.request.FragranceSearchCond;
import server.acode.domain.family.dto.response.FragranceByCatgegory;
import server.acode.domain.family.dto.response.QFragranceByCatgegory;


import java.util.List;

import static server.acode.domain.family.entity.QFamily.*;
import static server.acode.domain.family.entity.QFragranceFamily.*;
import static server.acode.domain.fragrance.entity.QBrand.*;
import static server.acode.domain.fragrance.entity.QFragrance.*;

@Repository
public class FamilyRepositoryImpl implements FamilyRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public FamilyRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<FragranceByCatgegory> search(FragranceSearchCond cond) {
        return queryFactory
                .select(new QFragranceByCatgegory(
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
                .where(family.korName.eq(cond.getFamily()),
                        fragrance.poster.isNotNull().and(fragrance.poster.ne(""))
                )
                .orderBy(fragrance.view.desc())
                .limit(6)
                .fetch();
    }
}
