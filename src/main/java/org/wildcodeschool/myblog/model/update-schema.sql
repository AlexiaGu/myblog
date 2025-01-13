CREATE TABLE article
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    title      VARCHAR(50) NOT NULL,
    content    TEXT NULL,
    created_at datetime    NOT NULL,
    updated_at datetime    NOT NULL,
    CONSTRAINT pk_article PRIMARY KEY (id)
);