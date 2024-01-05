package server.acode.domain.family.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FragranceFragranceFamilyRepositoryImpl implements FragranceFamilyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

}
