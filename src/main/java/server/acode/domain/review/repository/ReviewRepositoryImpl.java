package server.acode.domain.review.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.dto.response.QReviewInfo;
import server.acode.domain.fragrance.dto.response.QReviewPreview;
import server.acode.domain.fragrance.dto.response.ReviewInfo;
import server.acode.domain.fragrance.dto.response.ReviewPreview;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.user.dto.response.ReviewDto;
import server.acode.domain.user.dto.response.QDisplayReview;

import java.util.List;

import static server.acode.domain.review.entity.QReview.*;
import static server.acode.domain.user.entity.QUser.*;

@Repository
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ReviewPreview> getFragranceReviewPreview(Fragrance fragrance) {
        return queryFactory
                .select(new QReviewPreview(
                        review.comment,
                        review.rate,
                        review.thumbnail,
                        user.nickname
                ))
                .from(review)
                .join(review.user, user)
                .where(review.fragrance.eq(fragrance))
                .orderBy(review.createdAt.desc())
                .limit(3)
                .fetch();
    }

    @Override
    public Page<ReviewInfo> getReviewInfoPage(Long fragranceId, Pageable pageable) {
        List<ReviewInfo> contents = queryFactory
                .select(new QReviewInfo(
                        review.id.as("reviewId"),
                        review.thumbnail,
                        review.image1,
                        review.image2,

                        review.rate,
                        review.user.nickname,
                        review.comment,
                        review.textReview,

                        review.season,
                        review.longevity,
                        review.intensity,
                        review.style
                ))
                .from(review)
                .where(
                        review.fragrance.id.eq(fragranceId)
                )
                .orderBy(review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(review.count())
                .from(review)
                .where(review.fragrance.id.eq(fragranceId));

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<ReviewDto> getDisplayReview(Long userId, Pageable pageable){
        QueryResults<ReviewDto> results = queryFactory
                .select(new QDisplayReview(
                        review.id,
                        review.fragrance.id,
                        review.fragrance.name,
                        review.fragrance.brand.korName,
                        review.comment,
                        review.rate,
                        review.thumbnail
                ))
                .from(review)
                .join(review.user, user)
                .where(user.id.eq(userId))
                .orderBy(review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ReviewDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);

    }
}
