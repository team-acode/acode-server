package server.acode.domain.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import server.acode.domain.fragrance.dto.response.QReviewPreview;
import server.acode.domain.fragrance.dto.response.ReviewPreview;
import server.acode.domain.fragrance.entity.Fragrance;
import server.acode.domain.review.entity.QReview;
import server.acode.domain.review.entity.Review;
import server.acode.domain.user.entity.QUser;

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
}
