-- ChangeSet 2024-05-30--001--comments
create table comments
(
    id bigserial not null,
    description varchar(255),
    book_id bigint not null,
    PRIMARY KEY (id),
    FOREIGN KEY (book_id) REFERENCES books(id) on delete cascade
)