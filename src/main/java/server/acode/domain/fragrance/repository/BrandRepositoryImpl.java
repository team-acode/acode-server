package server.acode.domain.fragrance.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.dto.response.BrandInfo;
import server.acode.domain.fragrance.dto.response.QBrandInfo;

import java.util.List;

import static server.acode.domain.fragrance.entity.QBrand.*;

@Repository
public class BrandRepositoryImpl implements BrandRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public BrandRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<BrandInfo> searchBrand(String search, Pageable pageable) {
        List<BrandInfo> contents = queryFactory
                .select(new QBrandInfo(
                        brand.id.as("brandId"),
                        brand.korName.as("brandName"),
                        brand.roundImg
                ))
                .from(brand)
                .where(
                        brandNameContains(search)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(brand.count())
                .from(brand)
                .where(
                        brandNameContains(search)
                );

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    private BooleanExpression brandNameContains(String search) {
        return brand.korName.containsIgnoreCase(search)
                .or(brand.engName.containsIgnoreCase(search));
    }
}
