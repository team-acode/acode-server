package server.acode.domain.fragrance.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
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
    public List<BrandInfo> searchBrand(String search) {
        return queryFactory
                .select(new QBrandInfo(
                        brand.id.as("brandId"),
                        brand.korName,
                        brand.roundImg
                ))
                .from(brand)
                .where(
                        brand.korName.containsIgnoreCase(search)
                                .or(brand.engName.containsIgnoreCase(search))
                )
                .fetch();
    }
}
