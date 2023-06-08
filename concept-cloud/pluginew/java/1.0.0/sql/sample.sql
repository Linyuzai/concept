begin;

create table t_user
(
    id          varchar(64)  not null
        primary key,
    username    varchar(255) not null,
    password    varchar(255) not null,
    nickname    varchar(255) not null,
    avatar      varchar(255) null,
    enabled     tinyint(1) default 1 not null,
    create_time datetime     not null
);

INSERT INTO t_user (id, username, password, nickname, avatar, enabled, create_time)
VALUES ('user1', 'user1', 'user1', 'user1', null, 1, '2001-01-01 00:00:00');
INSERT INTO t_user (id, username, password, nickname, avatar, enabled, create_time)
VALUES ('user2', 'user2', 'user2', 'user2', null, 1, '2002-02-02 00:00:00');
INSERT INTO t_user (id, username, password, nickname, avatar, enabled, create_time)
VALUES ('user3', 'user3', 'user3', 'user3', null, 1, '2003-03-03 00:00:00');
INSERT INTO t_user (id, username, password, nickname, avatar, enabled, create_time)
VALUES ('user4', 'user4', 'user4', 'user4', null, 1, '2004-04-04 00:00:00');
INSERT INTO t_user (id, username, password, nickname, avatar, enabled, create_time)
VALUES ('user5', 'user5', 'user5', 'user5', null, 1, '2005-05-05 00:00:00');

create table t_sample
(
    id      varchar(64)  not null
        primary key,
    sample  varchar(255) not null,
    user_id varchar(64) null,
    deleted tinyint(1) default 0 not null
);

INSERT INTO t_sample (id, sample, user_id, deleted)
VALUES ('sample1', 'sample1', 'user1', 0);
INSERT INTO t_sample (id, sample, user_id, deleted)
VALUES ('sample2', 'sample2', 'user2', 0);

create table t_sample_user
(
    id        bigint auto_increment
        primary key,
    sample_id varchar(64) not null,
    user_id   varchar(64) not null
);

INSERT INTO t_sample_user (id, sample_id, user_id)
VALUES (1, 'sample1', 'user3');
INSERT INTO t_sample_user (id, sample_id, user_id)
VALUES (2, 'sample1', 'user4');
INSERT INTO t_sample_user (id, sample_id, user_id)
VALUES (3, 'sample2', 'user3');
INSERT INTO t_sample_user (id, sample_id, user_id)
VALUES (4, 'sample2', 'user5');

commit;
