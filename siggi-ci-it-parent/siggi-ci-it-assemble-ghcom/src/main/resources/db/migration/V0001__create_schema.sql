--
--
--
create table users(
    username text not null primary key,
    password text not null,
    enabled boolean not null
);

create table authorities (
    username text not null,
    authority text not null,
    constraint fk_authorities_users foreign key(username) references users(username)
);

create unique index ix_auth_username on authorities (username,authority);


--
-- create table(s) for Spring-Social-ConnectionRepository
-- org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository
-- do not change this
--
create table UserConnection (
    userId              varchar(255) not null,
    providerId          varchar(255) not null,
    providerUserId      varchar(255),
    rank                int not null,
    displayName         varchar(255),
    profileUrl          varchar(512),
    imageUrl            varchar(512),
    accessToken         varchar(255) not null,
    secret              varchar(255),
    refreshToken        varchar(255),
    expireTime          bigint,
    PRIMARY KEY (userId, providerId, providerUserId)
);

CREATE UNIQUE INDEX UserConnectionRank ON UserConnection(userId, providerId, rank);

--
--
create table build (
    id  bigserial not null,
    version int4,
    author text,
    author_email text,
    branch text,
    commit_id text,
    message text,
    raw_hook_payload text,
    url text,
    buildnumber int8 not null,
    end_time int8 not null,
    start_time int8 not null,
    orga text not null,
    provider text not null,
    repo text not null,
    state text not null,
    primary key (id)
);
--
--
create table build_slot (
    id  bigserial not null,
    version int4,
    end_time int8 not null,
    start_time int8 not null,
    matrix_discriminator text,
    slot int4 not null,
    state text,
    build_ref int8,
    primary key (id)
);
--
--
create table counter (
    id text not null,
    atom_long text,
    version int4,
    primary key (id)
);
--
--
create table deploy_key_entity (
    id text not null,
    fingerprint text,
    private_key text,
    public_key text,
    primary key (id)
);
--
--
create table environment_setting (
    project_id int8 not null,
    hidden boolean not null,
    key text,
    value text
);
--
--
create table project (
    id  bigserial not null,
    version int4,
    access_token text,
    active boolean not null,
    deploy_key_ref text,
    encryption_key text,
    orga text not null,
    provider text not null,
    repo text not null,
    private_repo boolean not null,
    scm_url text,
    web_url text,
    primary key (id)
);
--
--
alter table build add constraint build_id_unique_constraint unique (provider, orga, repo, buildnumber);

alter table build_slot add constraint build_ref_fk foreign key (build_ref) references build;

alter table environment_setting add constraint project_id_fk foreign key (project_id) references project;

alter table project add constraint repo_id_unique_constraint unique (provider, orga, repo);
