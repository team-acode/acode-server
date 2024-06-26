package server.acode.domain.user.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import server.acode.domain.user.dto.response.*;

import java.util.List;

import static server.acode.domain.fragrance.entity.QFragrance.*;
import static server.acode.domain.user.entity.QScrap.*;
import static server.acode.domain.user.entity.QUser.*;

@Repository
public class ScrapRepositoryImpl implements ScrapRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public ScrapRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ScrapPreviewDto> getScrapPreview(Long userId){
        return queryFactory
                .select(new QScrapPreviewDto(
                        fragrance.id,
                        fragrance.thumbnail
                ))
                .from(scrap)
                .join(scrap.fragrance, fragrance)
                .join(scrap.user, user)
                .where(user.id.eq(userId))
                .orderBy(scrap.createdAt.desc())
                .limit(3)
                .fetch();
    }

    @Override
    public Page<ScrapDto> getScrap(Long userId, Pageable pageable){
        QueryResults<ScrapDto> results = queryFactory
                .select(new QScrapDto(
                        scrap.fragrance.id.as("fragranceId"),
                        scrap.fragrance.name.as("fragranceName"),
                        scrap.fragrance.brand.korName.as("brandName"),
                        scrap.fragrance.concentration,
                        scrap.fragrance.thumbnail
                ))
                .from(scrap)
                .join(scrap.user, user)
                .where(user.id.eq(userId))
                .orderBy(scrap.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ScrapDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


}
