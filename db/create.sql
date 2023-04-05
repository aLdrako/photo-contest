create or replace table photo_contest.contests
(
    id              bigint auto_increment
        primary key,
    title           varchar(50)          not null,
    category_id     bigint               not null,
    is_invitational tinyint(1) default 0 not null,
    phase1          datetime             not null,
    phase2          datetime             not null,
    constraint contests_pk2
        unique (title)
);

create or replace table photo_contest.cover_photos
(
    contest_id  bigint not null
        primary key,
    cover_photo blob   null,
    constraint cover_photos_contests_fk
        foreign key (contest_id) references photo_contest.contests (id)
);

create or replace table photo_contest.rankings
(
    id   int auto_increment
        primary key,
    name varchar(50) not null,
    constraint rankings_pk2
        unique (name)
);

create or replace table photo_contest.users
(
    id         bigint auto_increment
        primary key,
    first_name varchar(32)                          not null,
    last_name  varchar(32)                          not null,
    username   varchar(50)                          not null,
    email      varchar(50)                          not null,
    password   varchar(100)                         not null,
    join_date  datetime default current_timestamp() not null,
    ranking_id int      default 1                   null,
    constraint users_pk2
        unique (username),
    constraint users_pk3
        unique (email),
    constraint users_rankings_id_fk
        foreign key (ranking_id) references photo_contest.rankings (id)
);

create or replace table photo_contest.juries
(
    contest_id bigint not null,
    user_id    bigint not null,
    primary key (contest_id, user_id),
    constraint juries_contests_fk
        foreign key (contest_id) references photo_contest.contests (id),
    constraint juries_users_id_fk
        foreign key (user_id) references photo_contest.users (id)
);

create or replace table photo_contest.participants
(
    contest_id bigint not null,
    user_id    bigint not null,
    primary key (contest_id, user_id),
    constraint participants_contests_id_fk
        foreign key (contest_id) references photo_contest.contests (id),
    constraint participants_users_id_fk
        foreign key (user_id) references photo_contest.users (id)
);

create or replace table photo_contest.permissions
(
    user_id      bigint               not null
        primary key,
    is_organizer tinyint(1) default 0 not null,
    id_deleted   tinyint(1) default 0 not null,
    constraint permissions_users_fk
        foreign key (user_id) references photo_contest.users (id)
);

create or replace table photo_contest.photos
(
    id      bigint auto_increment
        primary key,
    title   varchar(50)   not null,
    story   varchar(8196) not null,
    photo   blob          not null,
    user_id bigint        not null,
    constraint photos_users_fk
        foreign key (user_id) references photo_contest.users (id)
);

create or replace table photo_contest.contests_photos
(
    contest_id bigint not null,
    photo_id   bigint not null,
    primary key (contest_id, photo_id),
    constraint contests_photos_contests_fk
        foreign key (contest_id) references photo_contest.contests (id),
    constraint contests_photos_photos_id_fk
        foreign key (photo_id) references photo_contest.photos (id)
);

create or replace table photo_contest.points
(
    user_id bigint not null
        primary key,
    points  int    null,
    constraint points_users_fk
        foreign key (user_id) references photo_contest.users (id)
);
