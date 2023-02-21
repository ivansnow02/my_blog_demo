create database blog;
use blog;
CREATE TABLE `m_user`
(
    `id`         int NOT NULL AUTO_INCREMENT,
    `username`   varchar(64)  DEFAULT NULL,
    `avatar`     varchar(255) DEFAULT NULL,
    `email`      varchar(64)  DEFAULT NULL,
    `password`   varchar(64)  DEFAULT NULL,
    `created`    datetime     DEFAULT NULL,
    `last_login` datetime     DEFAULT NULL,
    `roles`      varchar(10)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `UK_USERNAME` (`username`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
CREATE TABLE `m_blog`
(
    `id`          int          NOT NULL AUTO_INCREMENT,
    `user_id`     int          NOT NULL,
    `title`       varchar(255) NOT NULL,
    `description` varchar(255) NOT NULL,
    `content`     longtext,
    `created`     datetime   DEFAULT NULL,
    `status`      tinyint(4) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8mb4;
