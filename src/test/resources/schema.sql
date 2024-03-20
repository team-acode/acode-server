create table `user`(
    user_id bigint primary key,
    role varchar(50),
    auth_key varchar(30),
    nickname varchar(30),
    review_cnt integer,
    is_del tinyint default 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
