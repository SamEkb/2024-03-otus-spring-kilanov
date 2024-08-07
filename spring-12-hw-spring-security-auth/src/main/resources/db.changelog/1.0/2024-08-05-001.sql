-- ChangeSet 2024-08-05--001--users
create table users
(
    id bigserial not null,
    username varchar(255) not null,
    password varchar(255) not null,
    PRIMARY KEY (id)
)