CREATE SCHEMA IF NOT EXISTS blog;

CREATE TABLE IF NOT EXISTS blog.tags
(
    tag_id BIGSERIAL PRIMARY KEY,
    tag    varchar(25)
);

CREATE TABLE IF NOT EXISTS blog.posts
(
    post_id   BIGSERIAL PRIMARY KEY,
    title     varchar(150),
    text      varchar(150),
    likeCount int default 0
);

CREATE TABLE IF NOT EXISTS blog.comments
(
    comment_id BIGSERIAL PRIMARY KEY,
    text       varchar(150)
);

CREATE TABLE IF NOT EXISTS blog.posts_comments (
    comment_id bigint,
    post_id bigint,
    PRIMARY KEY (comment_id, post_id),
    CONSTRAINT fk_posts_comments_1 foreign key (comment_id) references blog.comments(comment_id) ON DELETE CASCADE ,
    CONSTRAINT fk_posts_comments_2 foreign key (post_id) references blog.posts(post_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS blog.posts_tags (
    tag_id bigint,
    post_id bigint,
    primary key (tag_id, post_id),
    CONSTRAINT fk_posts_tags_1 foreign key (tag_id) references blog.tags(tag_id) ON DELETE CASCADE ,
    CONSTRAINT fk_posts_tags_2 foreign key (post_id) references blog.posts(post_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS blog.images (
    image_id BIGSERIAL PRIMARY KEY,
    post_id BIGINT,
    image BYTEA,
    CONSTRAINT unique_post_id UNIQUE (post_id),
    CONSTRAINT fk_image_post foreign key (post_id) references blog.posts(post_id) ON DELETE CASCADE
 );