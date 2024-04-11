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

CREATE TABLE brand (
    brand_id BIGINT PRIMARY KEY,
    kor_name VARCHAR(50),
    eng_name VARCHAR(50),
    summary TEXT,
    keyword VARCHAR(255),
    background_img VARCHAR(255),
    round_img VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table fragrance(
    fragrance_id bigint primary key,
    name varchar(50),
    brand_id BIGINT,
    rate_sum INT DEFAULT 0,
    review_cnt INT DEFAULT 0,
    concentration varchar(50),
    is_single TINYINT DEFAULT 0,
    view INT DEFAULT 0,
    poster VARCHAR(255),
    link1 VARCHAR(255),
    link2 VARCHAR(255),
    link3 VARCHAR(255),
    thumbnail VARCHAR(255),
    image1 VARCHAR(255),
    image2 VARCHAR(255),
    style VARCHAR(255),
    season VARCHAR(255),
    scent VARCHAR(255),
    version BIGINT DEFAULT 0L,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_brand FOREIGN KEY (brand_id) REFERENCES brand(brand_id)
);

CREATE TABLE review (
     review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
     rate INT NOT NULL,
     comment VARCHAR(100) NOT NULL,
     season varchar(50),
     longevity varchar(50),
     intensity varchar(50),
     style VARCHAR(20),
     text_review VARCHAR(4000),
     thumbnail VARCHAR(255),
     image1 VARCHAR(255),
     image2 VARCHAR(255),
     is_del TINYINT DEFAULT 0,
     user_id BIGINT,
     fragrance_id BIGINT,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     FOREIGN KEY (user_id) REFERENCES `user`(user_id),
     FOREIGN KEY (fragrance_id) REFERENCES fragrance(fragrance_id)
);

CREATE TABLE review_intensity (
     review_intensity_id BIGINT PRIMARY KEY,
     weak INT DEFAULT 0,
     medium INT DEFAULT 0,
     strong INT DEFAULT 0,
     intense INT DEFAULT 0,
     fragrance_id BIGINT,
     version BIGINT DEFAULT 0L,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     FOREIGN KEY (fragrance_id) REFERENCES fragrance(fragrance_id)
);

CREATE TABLE review_longevity (
     review_longevity_id BIGINT PRIMARY KEY,
     onehour INT DEFAULT 0,
     fourhours INT DEFAULT 0,
     halfday INT DEFAULT 0,
     fullday INT DEFAULT 0,
     fragrance_id BIGINT,
     version BIGINT DEFAULT 0L,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     FOREIGN KEY (fragrance_id) REFERENCES fragrance(fragrance_id)
);


CREATE TABLE review_season (
     review_season_id BIGINT PRIMARY KEY,
     spring INT DEFAULT 0,
     summer INT DEFAULT 0,
     autumn INT DEFAULT 0,
     winter INT DEFAULT 0,
     all_seasons INT DEFAULT 0,
     fragrance_id BIGINT,
     version BIGINT DEFAULT 0L,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     FOREIGN KEY (fragrance_id) REFERENCES fragrance(fragrance_id)
);


CREATE TABLE review_style (
     review_style_id BIGINT PRIMARY KEY,
     chic INT DEFAULT 0,
     mature INT DEFAULT 0,
     luxurious INT DEFAULT 0,
     elegant INT DEFAULT 0,
     masculine INT DEFAULT 0,
     comfortable INT DEFAULT 0,
     serene INT DEFAULT 0,
     light INT DEFAULT 0,
     neutral INT DEFAULT 0,
     friendly INT DEFAULT 0,
     clean INT DEFAULT 0,
     sensual INT DEFAULT 0,
     delicate INT DEFAULT 0,
     lively INT DEFAULT 0,
     lovely INT DEFAULT 0,
     bright INT DEFAULT 0,
     radiant INT DEFAULT 0,
     feminine INT DEFAULT 0,
     innocent INT DEFAULT 0,
     weighty INT DEFAULT 0,
     soft INT DEFAULT 0,
     cozy INT DEFAULT 0,
     fragrance_id BIGINT,
     version BIGINT DEFAULT 0L,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     FOREIGN KEY (fragrance_id) REFERENCES fragrance(fragrance_id)
);

CREATE TABLE scrap (
     scrap_id BIGINT AUTO_INCREMENT PRIMARY KEY,
     user_id BIGINT,
     fragrance_id BIGINT,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     FOREIGN KEY (user_id) REFERENCES `user`(user_id),
     FOREIGN KEY (fragrance_id) REFERENCES fragrance(fragrance_id)
);

