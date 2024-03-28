insert into `user` (user_id, role, auth_key, nickname, review_cnt, is_del) values (1L, 'ROLE_USER', 'testauthkey1', 'nickname1', 0, 0);
insert into `user` (user_id, role, auth_key, nickname, review_cnt, is_del) values (2L, 'ROLE_USER', 'testauthkey2', 'nickname2', 0, 0);

insert into brand(brand_id, kor_name) values (1L, '톰포드');
insert into fragrance(fragrance_id, name, brand_id, concentration) values (1L, '톰포드향수', 1L, 'EDC');

insert into review(rate, comment, season, longevity, intensity, style, user_id, fragrance_id)
values (5, '아주 좋아요', 'SPRING', 'ONEHOUR', 'WEAK', 'CHIC', 1L, 1L);

insert into review_intensity(review_intensity_id, fragrance_id, weak) values (1L, 1L, 1);
insert into review_longevity(review_longevity_id, fragrance_id, onehour) values (1L, 1L, 1);
insert into review_season(review_season_id, fragrance_id, spring) values (1L, 1L, 1);
insert into review_style(review_style_id, fragrance_id, chic) values (1L, 1L, 1);