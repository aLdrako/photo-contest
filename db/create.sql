create or replace table categories
(
    id   bigint auto_increment
        primary key,
    name varchar(16) not null
);

create or replace table contests
(
    id              bigint auto_increment
        primary key,
    title           varchar(50)                            not null,
    category_id     bigint                                 not null,
    is_invitational tinyint(1) default 0                   not null,
    phase1          datetime                               not null,
    phase2          datetime                               not null,
    date_created    datetime   default current_timestamp() not null,
    constraint contests_pk
        unique (title),
    constraint contests_pk2
        unique (title),
    constraint contests_categories_id_fk
        foreign key (category_id) references categories (id)
);

create or replace table cover_photos
(
    contest_id  bigint not null
        primary key,
    cover_photo blob   null,
    constraint cover_photos_contests_fk
        foreign key (contest_id) references contests (id)
);

create or replace table rankings
(
    id   int auto_increment
        primary key,
    name varchar(50) not null,
    constraint rankings_pk2
        unique (name)
);

create or replace table users
(
    id         bigint auto_increment
        primary key,
    first_name varchar(32)                          not null,
    last_name  varchar(32)                          not null,
    username   varchar(50)                          not null,
    email      varchar(50)                          not null,
    password   varchar(100)                         not null,
    join_date  datetime default current_timestamp() null,
    ranking_id int      default 1                   not null,
    points     int      default 0                   not null,
    constraint users_pk2
        unique (username),
    constraint users_pk3
        unique (email),
    constraint users_rankings_id_fk
        foreign key (ranking_id) references rankings (id)
);

create or replace table juries
(
    contest_id bigint not null,
    user_id    bigint not null,
    primary key (contest_id, user_id),
    constraint juries_contests_fk
        foreign key (contest_id) references contests (id),
    constraint juries_users_id_fk
        foreign key (user_id) references users (id)
);

create or replace table participants
(
    contest_id bigint not null,
    user_id    bigint not null,
    primary key (contest_id, user_id),
    constraint participants_contests_id_fk
        foreign key (contest_id) references contests (id),
    constraint participants_users_id_fk
        foreign key (user_id) references users (id)
);

create or replace table permissions
(
    user_id      bigint               not null
        primary key,
    is_organizer tinyint(1) default 0 not null,
    is_deleted   tinyint(1) default 0 not null,
    constraint permissions_users_fk
        foreign key (user_id) references users (id)
);

create or replace table photos
(
    id         bigint auto_increment
        primary key,
    title      varchar(50)   not null,
    story      varchar(8196) not null,
    photo      blob          not null,
    user_id    bigint        not null,
    contest_id bigint        not null,
    constraint photos_contests_id_fk
        foreign key (contest_id) references contests (id),
    constraint photos_users_fk
        foreign key (user_id) references users (id)
);

create or replace table photos_reviews
(
    photo_id      bigint               not null,
    jury_id       bigint               not null,
    comment       varchar(1024)        not null,
    score         int        default 3 not null,
    fits_category tinyint(1) default 1 not null,
    primary key (photo_id, jury_id),
    constraint photos_reviews_photos_id_fk
        foreign key (photo_id) references photos (id),
    constraint photos_reviews_users_id_fk
        foreign key (jury_id) references users (id)
);