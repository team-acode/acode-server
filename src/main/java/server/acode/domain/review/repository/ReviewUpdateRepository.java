package server.acode.domain.review.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import static server.acode.domain.review.entity.QReviewIntensity.reviewIntensity;
import static server.acode.domain.review.entity.QReviewLongevity.reviewLongevity;
import static server.acode.domain.review.entity.QReviewSeason.reviewSeason;
import static server.acode.domain.review.entity.QReviewStyle.reviewStyle;

@Repository
public class ReviewUpdateRepository {
    private final JPAQueryFactory queryFactory;

    public ReviewUpdateRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void updateSeason(String season, long fragranceId, int value){
        NumberPath<Integer> dynamicPath = Expressions.numberPath(int.class, reviewSeason, season);

        // 업데이트 쿼리 작성
        queryFactory.update(reviewSeason)
                .where(reviewSeason.fragrance.id.eq(fragranceId))
                .set(dynamicPath, dynamicPath.add(value))
                .execute();

    }

    public void updateLongevity(String longevity, long fragranceId, int value){
        NumberPath<Integer> dynamicPath = Expressions.numberPath(int.class, reviewLongevity, longevity);

        // 업데이트 쿼리 작성
        queryFactory.update(reviewLongevity)
                .where(reviewLongevity.fragrance.id.eq(fragranceId))
                .set(dynamicPath, dynamicPath.add(value))
                .execute();
    }

    public void updateIntensity(String intensity, long fragranceId, int value){
        NumberPath<Integer> dynamicPath = Expressions.numberPath(int.class, reviewIntensity, intensity);

        // 업데이트 쿼리 작성
        queryFactory.update(reviewIntensity)
                .where(reviewIntensity.fragrance.id.eq(fragranceId))
                .set(dynamicPath, dynamicPath.add(value))
                .execute();
    }

    public void updateStyle(String style, long fragranceId, int value){
        NumberPath<Integer> dynamicPath = Expressions.numberPath(int.class, reviewStyle, style);

        // 업데이트 쿼리 작성
        queryFactory.update(reviewStyle)
                .where(reviewStyle.fragrance.id.eq(fragranceId))
                .set(dynamicPath, dynamicPath.add(value))
                .execute();
    }


}
